package com.ninjaone.backendinterviewproject.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ninjaone.backendinterviewproject.application.BackendInterviewProjectApplication;
import com.ninjaone.backendinterviewproject.application.dtos.CustomerDTO;
import com.ninjaone.backendinterviewproject.domain.customer.service.CustomerService;
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
import org.springframework.util.Base64Utils;

import javax.persistence.EntityNotFoundException;
import java.util.NoSuchElementException;

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
@WebMvcTest(CustomerController.class)
@AutoConfigureMockMvc
@AutoConfigureDataJpa
public class CustomerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private CustomerService customerService;

    private CustomerDTO customerDTO;

    public static final String USER_SECRET = "test:backend";
    public static final String BASIC_AUTH = "Basic " + Base64Utils.encodeToString(USER_SECRET.getBytes());
    public static final String ID = "12345";

    @BeforeEach
    void setup() {
        customerDTO = CustomerDTO.builder().name("Test Customer").build();
    }

    @Test
    void shouldGetCustomerById() throws Exception {
        when(customerService.findById(ID)).thenReturn(customerDTO.toDomain());

        mockMvc.perform(get("/customers/" + ID).header(HttpHeaders.AUTHORIZATION, BASIC_AUTH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(customerDTO)));
    }

    @Test
    void shouldThrowExceptionWhenGetCustomerByIdAndCustomerIdIsNotFound() throws Exception {
        when(customerService.findById(ID)).thenThrow(NoSuchElementException.class);

        mockMvc.perform(get("/customers/" + ID).header(HttpHeaders.AUTHORIZATION, BASIC_AUTH))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateACustomer() throws Exception {
        when(customerService.save(any())).thenReturn(customerDTO.toDomain());

        String customerString = objectMapper.writeValueAsString(customerDTO);

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerString)
                        .header(HttpHeaders.AUTHORIZATION, BASIC_AUTH))
                .andExpect(status().isCreated())
                .andExpect(content().string(customerString));
    }

    @Test
    void shouldUpdateACustomer() throws Exception {
        var updatedCustomer = customerDTO;
        updatedCustomer.setName("New System Name");

        when(customerService.update(any(), any())).thenReturn(updatedCustomer.toDomain());

        String customerString = objectMapper.writeValueAsString(updatedCustomer);

        mockMvc.perform(put("/customers/" + ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerString)
                        .header(HttpHeaders.AUTHORIZATION, BASIC_AUTH))
                .andExpect(status().isOk())
                .andExpect(content().string(customerString));
    }

    @Test
    void shouldThrowExceptionWhenUpdateCustomerAndCustomerIdIsNotFound() throws Exception {
        when(customerService.update(any(), any())).thenThrow(EntityNotFoundException.class);

        var updatedCustomer = customerDTO;
        updatedCustomer.setName("New Customer Name");

        String customerString = objectMapper.writeValueAsString(updatedCustomer);

        mockMvc.perform(put("/customers/" + ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerString)
                        .header(HttpHeaders.AUTHORIZATION, BASIC_AUTH))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteACustomer() throws Exception {
        doNothing().when(customerService).delete(ID);

        mockMvc.perform(delete("/customers/" + ID).header(HttpHeaders.AUTHORIZATION, BASIC_AUTH))
                .andExpect(status().isNoContent());
    }
}
