package com.ninjaone.backendinterviewproject.domain.device.entities;

import com.ninjaone.backendinterviewproject.application.dtos.DeviceDTO;
import com.ninjaone.backendinterviewproject.domain.device.entities.enums.DeviceType;
import com.ninjaone.backendinterviewproject.domain.service.entities.ServiceEntity;
import com.ninjaone.backendinterviewproject.resources.customer.model.CustomerModel;
import com.ninjaone.backendinterviewproject.resources.device.model.DeviceModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
public class DeviceEntity {
    private String id;
    private String systemName;
    private DeviceType type;
    private List<ServiceEntity> services;
    private String customerId;

    public static final BigDecimal COST_BY_DEVICE = BigDecimal.valueOf(4.00);

    public DeviceModel toModel(CustomerModel customer) {
        return DeviceModel.builder()
                .id(String.valueOf(UUID.randomUUID()))
                .systemName(systemName)
                .type(type.name())
                .services(services != null ? services.stream()
                        .map(ServiceEntity::toModel).collect(Collectors.toList()) : null)
                .customer(customer)
                .build();
    }

    public DeviceDTO toDTO() {
        return DeviceDTO.builder()
                .id(id)
                .systemName(systemName)
                .type(type)
                .serviceIds(services != null ? services.stream()
                        .map(ServiceEntity::getId).collect(Collectors.toList()) : null)
                .customerId(customerId)
                .build();
    }

    public BigDecimal calculateCosts() {
        if (services == null) {
            return BigDecimal.ZERO;
        }

        List<BigDecimal> costs = services.stream().map(ServiceEntity::getCost).collect(Collectors.toList());
        BigDecimal sum = COST_BY_DEVICE;

        for (BigDecimal cost : costs) {
            sum = sum.add(cost);
        }

        return sum;
    }
}