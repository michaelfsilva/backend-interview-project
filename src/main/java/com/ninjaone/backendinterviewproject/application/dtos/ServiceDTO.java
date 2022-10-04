package com.ninjaone.backendinterviewproject.application.dtos;

import com.ninjaone.backendinterviewproject.domain.service.entities.ServiceEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ServiceDTO {
    private String id;
    @NotBlank(message = "type is mandatory")
    private String type;
    @NotNull(message = "cost is mandatory")
    private BigDecimal cost;

    public ServiceEntity toDomain() {
        return ServiceEntity.builder()
                .id(id)
                .type(type)
                .cost(cost)
                .build();
    }
}
