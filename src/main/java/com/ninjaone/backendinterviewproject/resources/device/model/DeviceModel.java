package com.ninjaone.backendinterviewproject.resources.device.model;

import com.ninjaone.backendinterviewproject.domain.device.entities.DeviceEntity;
import com.ninjaone.backendinterviewproject.domain.device.entities.enums.DeviceType;
import com.ninjaone.backendinterviewproject.resources.customer.model.CustomerModel;
import com.ninjaone.backendinterviewproject.resources.service.model.ServiceModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "devices")
public class DeviceModel {
    @Id
    private String id;
    private String systemName;
    private String type;
    @ManyToMany
    @JoinTable(
            name = "device_services",
            joinColumns = @JoinColumn(name = "device_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id"))
    private List<ServiceModel> services;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private CustomerModel customer;

    public DeviceEntity toDomain() {
        return DeviceEntity.builder()
                .id(id)
                .systemName(systemName)
                .type(DeviceType.valueOf(type))
                .services(services != null ? services.stream().map(ServiceModel::toDomain).collect(Collectors.toList()) : null)
                .customerId(customer.getId())
                .build();
    }
}
