package com.ninjaone.backendinterviewproject.application.dtos;

import com.ninjaone.backendinterviewproject.domain.device.entities.DeviceEntity;
import com.ninjaone.backendinterviewproject.domain.device.entities.enums.DeviceType;
import com.ninjaone.backendinterviewproject.domain.service.entities.ServiceEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class DeviceDTO {
    private String id;
    @NotBlank(message = "systemName is mandatory")
    private String systemName;
    private DeviceType type;
    private List<String> serviceIds;
    @NotBlank(message = "customerId is mandatory")
    private String customerId;

    public DeviceEntity toDomain() {
        return DeviceEntity.builder()
                .systemName(systemName)
                .type(type)
                .services(serviceIds != null ? serviceIds.stream()
                        .map(id -> ServiceEntity.builder().id(id).build()).collect(Collectors.toList()) : null)
                .customerId(customerId)
                .build();
    }
}
