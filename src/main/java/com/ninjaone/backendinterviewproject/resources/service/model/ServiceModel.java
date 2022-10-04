package com.ninjaone.backendinterviewproject.resources.service.model;

import com.ninjaone.backendinterviewproject.domain.service.entities.ServiceEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "services")
public class ServiceModel {
    @Id
    private String id;
    @Column(unique = true)
    private String type;
    private BigDecimal cost;

    public ServiceEntity toDomain() {
        return ServiceEntity.builder()
                .id(id)
                .type(type)
                .cost(cost)
                .build();
    }
}
