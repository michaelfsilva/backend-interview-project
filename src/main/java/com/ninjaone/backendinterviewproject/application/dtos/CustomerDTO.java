package com.ninjaone.backendinterviewproject.application.dtos;

import com.ninjaone.backendinterviewproject.domain.customer.entities.CustomerEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CustomerDTO {
    private String id;
    private String name;
    private List<DeviceDTO> devices;
    private List<ServiceDTO> services;

    public CustomerEntity toDomain() {
        return CustomerEntity.builder()
                .name(name)
                .build();
    }
}
