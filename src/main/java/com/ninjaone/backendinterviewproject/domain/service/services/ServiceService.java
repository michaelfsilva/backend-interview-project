package com.ninjaone.backendinterviewproject.domain.service.services;

import com.ninjaone.backendinterviewproject.domain.service.entities.ServiceEntity;
import com.ninjaone.backendinterviewproject.resources.service.database.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceService {
    @Autowired
    ServiceRepository serviceRepository;

    public ServiceEntity save(ServiceEntity serviceEntity) {
        return serviceRepository.save(
                serviceEntity.toModel()
        ).toDomain();
    }

    public void delete(String id) {
        serviceRepository.deleteById(id);
    }
}
