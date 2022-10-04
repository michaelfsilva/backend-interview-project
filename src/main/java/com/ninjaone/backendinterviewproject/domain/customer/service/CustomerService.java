package com.ninjaone.backendinterviewproject.domain.customer.service;

import com.ninjaone.backendinterviewproject.domain.customer.entities.CustomerEntity;
import com.ninjaone.backendinterviewproject.domain.device.entities.DeviceEntity;
import com.ninjaone.backendinterviewproject.resources.customer.database.CustomerRepository;
import com.ninjaone.backendinterviewproject.resources.customer.model.CustomerModel;
import com.ninjaone.backendinterviewproject.resources.device.database.DeviceRepository;
import com.ninjaone.backendinterviewproject.resources.device.model.DeviceModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CustomerService {
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    DeviceRepository deviceRepository;

    public CustomerEntity findById(String id) {
        return customerRepository.findById(id).orElseThrow().toDomain();
    }

    public CustomerEntity save(CustomerEntity customerEntity) {
        return customerRepository.save(customerEntity.toModel()).toDomain();
    }

    public CustomerEntity update(String id, CustomerEntity customerEntity) {
        CustomerModel customerModel = customerEntity.toModel();
        customerModel.setId(id);
        customerRepository.findById(id).orElseThrow();
        return customerRepository.save(customerModel).toDomain();
    }

    public void delete(String id) {
        customerRepository.deleteById(id);
    }

    public BigDecimal getMonthlyCostsById(String id) {
        var deviceList = deviceRepository.findByCustomerId(id);
        var costs = BigDecimal.ZERO;
        for (DeviceModel deviceModel : deviceList) {
            DeviceEntity device = deviceModel.toDomain();
            costs = costs.add(device.calculateCosts());
        }
        return costs;
    }
}
