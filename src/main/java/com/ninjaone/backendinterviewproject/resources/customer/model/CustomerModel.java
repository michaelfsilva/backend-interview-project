package com.ninjaone.backendinterviewproject.resources.customer.model;

import com.ninjaone.backendinterviewproject.domain.customer.entities.CustomerEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customers")
public class CustomerModel {
    @Id
    private String id;
    @Column(unique = true)
    private String name;

    public CustomerEntity toDomain() {
        return CustomerEntity.builder()
                .id(id)
                .name(name)
                .build();
    }
}
