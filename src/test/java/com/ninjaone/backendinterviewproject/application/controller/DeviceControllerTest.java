package com.ninjaone.backendinterviewproject.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ninjaone.backendinterviewproject.application.BackendInterviewProjectApplication;
import com.ninjaone.backendinterviewproject.application.dtos.DeviceDTO;
import com.ninjaone.backendinterviewproject.application.dtos.ServiceDTO;
import com.ninjaone.backendinterviewproject.domain.device.services.DeviceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static com.ninjaone.backendinterviewproject.application.controller.CustomerControllerTest.BASIC_AUTH;
import static com.ninjaone.backendinterviewproject.domain.device.entities.enums.DeviceType.WINDOWS_WORKSTATION;
import static com.ninjaone.backendinterviewproject.domain.service.entities.enums.ServiceType.ANTIVIRUS_WINDOWS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {BackendInterviewProjectApplication.class})
@WebMvcTest(DeviceController.class)
@AutoConfigureMockMvc
@AutoConfigureDataJpa
public class DeviceControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private DeviceService deviceService;

    private DeviceDTO deviceDTO;

    public static final String ID = "12345";

    @BeforeEach
    void setup() {
        ServiceDTO serviceDTO = ServiceDTO.builder().id("1").type(ANTIVIRUS_WINDOWS.name()).build();
        deviceDTO = DeviceDTO.builder()
                .systemName("Windows Workstation")
                .type(WINDOWS_WORKSTATION)
                .serviceIds(List.of(serviceDTO.getId()))
                .customerId(ID)
                .build();
    }

    @Test
    void shouldGetAllDevices() throws Exception {
        List<DeviceDTO> devices = Arrays.asList(deviceDTO, deviceDTO, deviceDTO);

        when(deviceService.findAll()).thenReturn(
                devices.stream().map(DeviceDTO::toDomain).collect(Collectors.toList())
        );

        mockMvc.perform(get("/devices").header(HttpHeaders.AUTHORIZATION, BASIC_AUTH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(devices)));
    }

    @Test
    void shouldGetDeviceById() throws Exception {
        when(deviceService.findById(ID)).thenReturn(deviceDTO.toDomain());

        mockMvc.perform(get("/devices/" + ID).header(HttpHeaders.AUTHORIZATION, BASIC_AUTH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(deviceDTO)));
    }

    @Test
    void shouldThrowExceptionWhenGetDeviceByIdAndDeviceIdIsNotFound() throws Exception {
        when(deviceService.findById(ID)).thenThrow(NoSuchElementException.class);

        mockMvc.perform(get("/devices/" + ID).header(HttpHeaders.AUTHORIZATION, BASIC_AUTH))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateADevice() throws Exception {
        when(deviceService.save(any())).thenReturn(deviceDTO.toDomain());

        String deviceString = objectMapper.writeValueAsString(deviceDTO);

        mockMvc.perform(post("/devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(deviceString)
                        .header(HttpHeaders.AUTHORIZATION, BASIC_AUTH))
                .andExpect(status().isCreated())
                .andExpect(content().string(deviceString));
    }

    @Test
    void shouldUpdateADevice() throws Exception {
        var updatedDevice = deviceDTO;
        updatedDevice.setSystemName("New System Name");

        when(deviceService.update(any(), any())).thenReturn(updatedDevice.toDomain());

        String deviceString = objectMapper.writeValueAsString(updatedDevice);

        mockMvc.perform(put("/devices/" + ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(deviceString)
                        .header(HttpHeaders.AUTHORIZATION, BASIC_AUTH))
                .andExpect(status().isOk())
                .andExpect(content().string(deviceString));
    }

    @Test
    void shouldThrowExceptionWhenUpdateDeviceAndDeviceIdIsNotFound() throws Exception {
        when(deviceService.update(any(), any())).thenThrow(EntityNotFoundException.class);

        var updatedDevice = deviceDTO;
        updatedDevice.setSystemName("New System Name");

        String deviceString = objectMapper.writeValueAsString(updatedDevice);

        mockMvc.perform(put("/devices/" + ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(deviceString)
                        .header(HttpHeaders.AUTHORIZATION, BASIC_AUTH))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteADevice() throws Exception {
        doNothing().when(deviceService).delete(ID);

        mockMvc.perform(delete("/devices/" + ID).header(HttpHeaders.AUTHORIZATION, BASIC_AUTH))
                .andExpect(status().isNoContent());
    }
}
