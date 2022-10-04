package com.ninjaone.backendinterviewproject.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ninjaone.backendinterviewproject.application.dtos.CustomerDTO;
import com.ninjaone.backendinterviewproject.application.dtos.DeviceDTO;
import com.ninjaone.backendinterviewproject.application.dtos.ServiceDTO;
import com.ninjaone.backendinterviewproject.resources.customer.database.CustomerRepository;
import com.ninjaone.backendinterviewproject.resources.customer.model.CustomerModel;
import com.ninjaone.backendinterviewproject.resources.device.database.DeviceRepository;
import com.ninjaone.backendinterviewproject.resources.device.model.DeviceModel;
import com.ninjaone.backendinterviewproject.resources.service.database.ServiceRepository;
import com.ninjaone.backendinterviewproject.resources.service.model.ServiceModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static com.ninjaone.backendinterviewproject.application.controller.CustomerControllerTest.BASIC_AUTH;
import static com.ninjaone.backendinterviewproject.domain.device.entities.enums.DeviceType.MAC;
import static com.ninjaone.backendinterviewproject.domain.device.entities.enums.DeviceType.WINDOWS_WORKSTATION;
import static com.ninjaone.backendinterviewproject.domain.service.entities.enums.ServiceType.ANTIVIRUS_MAC;
import static com.ninjaone.backendinterviewproject.domain.service.entities.enums.ServiceType.ANTIVIRUS_WINDOWS;
import static com.ninjaone.backendinterviewproject.domain.service.entities.enums.ServiceType.BACKUP;
import static com.ninjaone.backendinterviewproject.domain.service.entities.enums.ServiceType.SCREEN_SHARE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class DeviceIntegrationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    DeviceRepository deviceRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ServiceRepository serviceRepository;

    private CustomerModel customerModel;
    private DeviceDTO deviceDTO;

    public static final String ID = "12345";

    @BeforeEach
    void setup() {
        customerModel = CustomerModel.builder().id(ID).name("Test Customer").build();

        ServiceDTO serviceDTO = ServiceDTO.builder().id("1").type(ANTIVIRUS_WINDOWS.name()).cost(new BigDecimal("5.00")).build();
        deviceDTO = DeviceDTO.builder()
                .systemName("Windows Workstation")
                .type(WINDOWS_WORKSTATION)
                .serviceIds(List.of(serviceDTO.getId()))
                .customerId(ID)
                .build();
    }

    @AfterEach
    void cleanup() {
        deviceRepository.deleteAll();
        serviceRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    void shouldNotCreateDuplicateDevices() throws Exception {
        var serviceModel = ServiceModel.builder().id("1").type(ANTIVIRUS_WINDOWS.name()).cost(new BigDecimal("5.00")).build();
        var deviceModel = new DeviceModel(
                ID,
                "Windows Workstation",
                WINDOWS_WORKSTATION.name(),
                List.of(serviceModel),
                customerModel
        );

        serviceRepository.save(serviceModel);
        customerRepository.save(customerModel);
        deviceRepository.save(deviceModel);

        String deviceDTOString = objectMapper.writeValueAsString(this.deviceDTO);

        mockMvc.perform(post("/devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(deviceDTOString)
                        .header(HttpHeaders.AUTHORIZATION, BASIC_AUTH))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void shouldCalculateDeviceMonthlyCosts() throws Exception {
        var antivirusWindows = ServiceModel.builder().id("1").type(ANTIVIRUS_WINDOWS.name()).cost(new BigDecimal("5.00")).build();
        var antivirusMac = ServiceModel.builder().id("2").type(ANTIVIRUS_MAC.name()).cost(new BigDecimal("7.00")).build();
        var backup = ServiceModel.builder().id("3").type(BACKUP.name()).cost(new BigDecimal("3.00")).build();
        var screenShare = ServiceModel.builder().id("5").type(SCREEN_SHARE.name()).cost(new BigDecimal("1.00")).build();

        serviceRepository.saveAll(List.of(antivirusWindows, antivirusMac, backup, screenShare));
        customerRepository.save(customerModel);

        var deviceWindows1 = new DeviceModel(String.valueOf(UUID.randomUUID()), "deviceWindows1", WINDOWS_WORKSTATION.name(), List.of(antivirusWindows, backup, screenShare), customerModel);
        var deviceWindows2 = new DeviceModel(String.valueOf(UUID.randomUUID()), "deviceWindows2", WINDOWS_WORKSTATION.name(), List.of(antivirusWindows, backup, screenShare), customerModel);
        var deviceMac1 = new DeviceModel(String.valueOf(UUID.randomUUID()), "deviceMac1", MAC.name(), List.of(antivirusMac, backup, screenShare), customerModel);
        var deviceMac2 = new DeviceModel(String.valueOf(UUID.randomUUID()), "deviceMac2", MAC.name(), List.of(antivirusMac, backup, screenShare), customerModel);
        var deviceMac3 = new DeviceModel(String.valueOf(UUID.randomUUID()), "deviceMac3", MAC.name(), List.of(antivirusMac, backup, screenShare), customerModel);

        deviceRepository.saveAll(List.of(deviceWindows1, deviceWindows2, deviceMac1, deviceMac2, deviceMac3));

        mockMvc.perform(get("/customers/" + ID + "/costs").header(HttpHeaders.AUTHORIZATION, BASIC_AUTH))
                .andExpect(status().isOk())
                .andExpect(content().string("71.00"));
    }

    @Test
    void shouldCreateANewServiceAndCustomerAndDevice() throws Exception {
        var serviceDTORequest = ServiceDTO.builder()
                .type("NEW_ANTIVIRUS")
                .cost(new BigDecimal("7.50"))
                .build();

        String serviceDTOString = objectMapper.writeValueAsString(serviceDTORequest);

        MvcResult serviceResult = mockMvc.perform(post("/services")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serviceDTOString)
                        .header(HttpHeaders.AUTHORIZATION, BASIC_AUTH))
                .andExpect(status().isCreated())
                .andReturn();

        ServiceDTO serviceDTO = objectMapper.readValue(serviceResult.getResponse().getContentAsString(), ServiceDTO.class);

        var customerDTO = CustomerDTO.builder().name("New Customer").build();

        String customerString = objectMapper.writeValueAsString(customerDTO);

        MvcResult customerResult = mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerString)
                        .header(HttpHeaders.AUTHORIZATION, BASIC_AUTH))
                .andExpect(status().isCreated())
                .andReturn();

        CustomerDTO customerResponse
                = objectMapper.readValue(customerResult.getResponse().getContentAsString(), CustomerDTO.class);

        var deviceDTO = DeviceDTO.builder()
                .systemName("deviceWindows1")
                .type(WINDOWS_WORKSTATION)
                .serviceIds(List.of(serviceDTO.getId()))
                .customerId(customerResponse.getId())
                .build();

        String deviceDTOString = objectMapper.writeValueAsString(deviceDTO);

        MvcResult deviceResult = mockMvc.perform(post("/devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(deviceDTOString)
                        .header(HttpHeaders.AUTHORIZATION, BASIC_AUTH))
                .andExpect(status().isCreated())
                .andReturn();

        DeviceDTO deviceResponse = objectMapper.readValue(deviceResult.getResponse().getContentAsString(), DeviceDTO.class);

        assertEquals(deviceDTO.getSystemName(), deviceResponse.getSystemName());
        assertEquals(deviceDTO.getType(), deviceResponse.getType());
        assertEquals(deviceDTO.getCustomerId(), deviceResponse.getCustomerId());
    }
}
