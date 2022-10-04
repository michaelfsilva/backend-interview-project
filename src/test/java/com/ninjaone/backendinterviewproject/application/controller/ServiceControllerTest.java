package com.ninjaone.backendinterviewproject.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ninjaone.backendinterviewproject.application.BackendInterviewProjectApplication;
import com.ninjaone.backendinterviewproject.application.dtos.ServiceDTO;
import com.ninjaone.backendinterviewproject.domain.service.services.ServiceService;
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

import java.math.BigDecimal;

import static com.ninjaone.backendinterviewproject.application.controller.CustomerControllerTest.BASIC_AUTH;
import static com.ninjaone.backendinterviewproject.domain.service.entities.enums.ServiceType.ANTIVIRUS_WINDOWS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {BackendInterviewProjectApplication.class})
@WebMvcTest(ServiceController.class)
@AutoConfigureMockMvc
@AutoConfigureDataJpa
public class ServiceControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private ServiceService serviceService;

    private ServiceDTO serviceDTO;

    public static final String ID = "12345";

    @BeforeEach
    void setup() {
        serviceDTO = ServiceDTO.builder()
                .type(ANTIVIRUS_WINDOWS.name())
                .cost(BigDecimal.valueOf(5.00))
                .build();
    }

    @Test
    void shouldCreateAService() throws Exception {
        when(serviceService.save(any())).thenReturn(serviceDTO.toDomain());

        String serviceString = objectMapper.writeValueAsString(serviceDTO);

        mockMvc.perform(post("/services")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serviceString)
                        .header(HttpHeaders.AUTHORIZATION, BASIC_AUTH))
                .andExpect(status().isCreated())
                .andExpect(content().string(serviceString));
    }

    @Test
    void shouldDeleteAService() throws Exception {
        doNothing().when(serviceService).delete(ID);

        mockMvc.perform(delete("/services/" + ID).header(HttpHeaders.AUTHORIZATION, BASIC_AUTH))
                .andExpect(status().isNoContent());
    }
}
