package com.ninjaone.backendinterviewproject.domain.service.entities;

import com.ninjaone.backendinterviewproject.application.dtos.ServiceDTO;
import com.ninjaone.backendinterviewproject.resources.service.model.ServiceModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
public class ServiceEntity {
    private String id;
    private String type;
    private BigDecimal cost;

    public ServiceModel toModel() {
        return ServiceModel.builder()
                .id(id != null ? id : String.valueOf(UUID.randomUUID()))
                .type(type)
                .cost(cost)
                .build();
    }

    public ServiceDTO toDTO() {
        return ServiceDTO.builder()
                .id(id)
                .type(type)
                .cost(cost)
                .build();
    }
}
