package com.ninjaone.backendinterviewproject.domain.customer.entities;

import com.ninjaone.backendinterviewproject.application.dtos.CustomerDTO;
import com.ninjaone.backendinterviewproject.domain.device.entities.DeviceEntity;
import com.ninjaone.backendinterviewproject.domain.service.entities.ServiceEntity;
import com.ninjaone.backendinterviewproject.resources.customer.model.CustomerModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
public class CustomerEntity {
    private String id;
    private String name;
    private List<DeviceEntity> devices;
    private List<ServiceEntity> services;

    public CustomerModel toModel() {
        return CustomerModel.builder()
                .id(String.valueOf(UUID.randomUUID()))
                .name(name)
                .build();
    }

    public CustomerDTO toDTO() {
        return CustomerDTO.builder()
                .id(id)
                .name(name)
                .devices(devices != null ? devices.stream().map(DeviceEntity::toDTO).collect(Collectors.toList()) : null)
                .services(services != null ? services.stream().map(ServiceEntity::toDTO).collect(Collectors.toList()) : null)
                .build();
    }
}
