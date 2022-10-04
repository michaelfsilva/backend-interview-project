package com.ninjaone.backendinterviewproject.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ninjaone.backendinterviewproject.application.dtos.ServiceDTO;
import com.ninjaone.backendinterviewproject.domain.service.entities.enums.ServiceType;
import com.ninjaone.backendinterviewproject.resources.service.database.ServiceRepository;
import com.ninjaone.backendinterviewproject.resources.service.model.ServiceModel;
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

import java.math.BigDecimal;

import static com.ninjaone.backendinterviewproject.application.controller.CustomerControllerTest.BASIC_AUTH;
import static com.ninjaone.backendinterviewproject.domain.service.entities.enums.ServiceType.ANTIVIRUS_WINDOWS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ServiceIntegrationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ServiceRepository serviceRepository;

    private ServiceDTO serviceDTO;

    public static final String ID = "12345";

    @BeforeEach
    void setup() {
        serviceRepository.deleteAll();
        serviceDTO = ServiceDTO.builder()
                .type(ANTIVIRUS_WINDOWS.name())
                .cost(BigDecimal.valueOf(5.00))
                .build();
    }

    @Test
    void shouldNotCreateDuplicateServices() throws Exception {
        var serviceModel = ServiceModel.builder()
                .id(ID)
                .type(ServiceType.ANTIVIRUS_WINDOWS.name())
                .cost(BigDecimal.valueOf(5.00))
                .build();

        serviceRepository.save(serviceModel);

        String deviceEntityString = objectMapper.writeValueAsString(serviceDTO);

        mockMvc.perform(post("/services")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(deviceEntityString)
                        .header(HttpHeaders.AUTHORIZATION, BASIC_AUTH))
                .andExpect(status().isUnprocessableEntity());
    }
}
