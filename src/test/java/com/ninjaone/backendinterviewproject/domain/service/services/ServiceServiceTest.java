package com.ninjaone.backendinterviewproject.domain.service.services;

import com.ninjaone.backendinterviewproject.domain.service.entities.ServiceEntity;
import com.ninjaone.backendinterviewproject.domain.service.entities.enums.ServiceType;
import com.ninjaone.backendinterviewproject.resources.service.database.ServiceRepository;
import com.ninjaone.backendinterviewproject.resources.service.model.ServiceModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ServiceServiceTest {
    public static final String ID = "12345";

    @Mock
    private ServiceRepository serviceRepository;

    @InjectMocks
    private ServiceService serviceService;

    private ServiceModel serviceModel;

    @BeforeEach
    void setup() {
        serviceModel = ServiceModel.builder()
                .id(ID)
                .type(ServiceType.ANTIVIRUS_WINDOWS.name())
                .cost(BigDecimal.valueOf(5.00))
                .build();
    }

    @Test
    void shouldCreateAService() throws Exception {
        when(serviceRepository.save(any())).thenReturn(serviceModel);

        ServiceEntity savedService = serviceService.save(serviceModel.toDomain());

        assertEquals(serviceModel.getType(), savedService.getType());
        assertEquals(serviceModel.getCost(), savedService.getCost());
    }

    @Test
    void shouldDeleteAService() throws Exception {
        doNothing().when(serviceRepository).deleteById(ID);

        serviceService.delete(ID);
        Mockito.verify(serviceRepository, times(1)).deleteById(ID);
    }
}
