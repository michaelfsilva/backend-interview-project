package com.ninjaone.backendinterviewproject.domain.customer.services;

import com.ninjaone.backendinterviewproject.domain.customer.entities.CustomerEntity;
import com.ninjaone.backendinterviewproject.domain.customer.service.CustomerService;
import com.ninjaone.backendinterviewproject.resources.customer.database.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {
    public static final String ID = "12345";

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private CustomerEntity customerEntity;

    @BeforeEach
    void setup() {
        customerEntity = CustomerEntity.builder().name("Test Customer").build();
    }

    @Test
    void shouldGetCustomerById() throws Exception {
        when(customerRepository.findById(ID)).thenReturn(Optional.of(customerEntity.toModel()));

        CustomerEntity customer = customerService.findById(ID);

        assertNotNull(customer);
        assertEquals(customerEntity.getName(), customer.getName());
    }

    @Test
    void shouldThrowExceptionWhenGetCustomerByIdAndCustomerIdIsNotFound() throws Exception {
        when(customerRepository.findById(ID)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> customerService.findById(ID));
    }

    @Test
    void shouldCreateACustomer() throws Exception {
        when(customerRepository.save(any())).thenReturn(customerEntity.toModel());
        assertEquals(customerEntity.getName(), customerService.save(customerEntity).getName());
    }

    @Test
    void shouldUpdateACustomer() throws Exception {
        var updatedCustomer = customerEntity;
        updatedCustomer.setName("New Customer Name");

        when(customerRepository.findById(ID)).thenReturn(Optional.of(customerEntity.toModel()));
        when(customerRepository.save(any())).thenReturn(updatedCustomer.toModel());

        assertEquals(updatedCustomer.getName(), customerService.update(ID, updatedCustomer).getName());
    }

    @Test
    void shouldThrowExceptionWhenUpdateCustomerAndCustomerIdIsNotFound() throws Exception {
        when(customerRepository.findById(ID)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> customerService.update(ID, customerEntity));
    }

    @Test
    void shouldDeleteACustomer() throws Exception {
        doNothing().when(customerRepository).deleteById(ID);
        customerService.delete(ID);
        Mockito.verify(customerRepository, times(1)).deleteById(ID);
    }
}
