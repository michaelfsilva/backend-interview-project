package com.ninjaone.backendinterviewproject.domain.device.services;

import com.ninjaone.backendinterviewproject.domain.device.entities.DeviceEntity;
import com.ninjaone.backendinterviewproject.resources.customer.database.CustomerRepository;
import com.ninjaone.backendinterviewproject.resources.customer.model.CustomerModel;
import com.ninjaone.backendinterviewproject.resources.device.database.DeviceRepository;
import com.ninjaone.backendinterviewproject.resources.device.model.DeviceModel;
import com.ninjaone.backendinterviewproject.resources.service.database.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DeviceService {

    @Autowired
    DeviceRepository deviceRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ServiceRepository serviceRepository;

    public List<DeviceEntity> findAll() {
        return deviceRepository.findAll().stream().map(DeviceModel::toDomain).collect(Collectors.toList());
    }

    public DeviceEntity findById(String id) {
        return deviceRepository.findById(id).orElseThrow().toDomain();
    }

    public DeviceEntity save(DeviceEntity deviceEntity) {
        checkForDuplication(deviceEntity);

        Optional<CustomerModel> customerModel = customerRepository.findById(deviceEntity.getCustomerId());

        if (customerModel.isPresent()) {
            return deviceRepository.save(
                    deviceEntity.toModel(customerModel.get())
            ).toDomain();

        } else {
            throw new EntityNotFoundException("Customer not found for id: " + deviceEntity.getCustomerId());
        }
    }

    private void checkForDuplication(DeviceEntity deviceEntity) {
        if (deviceRepository.existsBySystemNameAndCustomerId(deviceEntity.getSystemName(), deviceEntity.getCustomerId())) {
            throw new DataIntegrityViolationException("The device '" + deviceEntity.getSystemName() + "' already exists in the database");
        }
    }

    public DeviceEntity update(String id, DeviceEntity deviceEntity) {
        Optional<CustomerModel> customerModel = customerRepository.findById(deviceEntity.getCustomerId());
        if (customerModel.isPresent()) {
            DeviceModel deviceModel = deviceEntity.toModel(customerModel.get());
            deviceModel.setId(id);
            deviceRepository.findById(id).orElseThrow();
            return deviceRepository.save(deviceModel).toDomain();
        } else {
            throw new EntityNotFoundException("Customer not found for id: " + deviceEntity.getCustomerId());
        }
    }

    public void delete(String id) {
        deviceRepository.deleteById(id);
    }
}
