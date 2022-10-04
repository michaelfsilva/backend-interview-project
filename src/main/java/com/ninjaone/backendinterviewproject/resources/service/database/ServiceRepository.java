package com.ninjaone.backendinterviewproject.resources.service.database;

import com.ninjaone.backendinterviewproject.resources.service.model.ServiceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceModel, String> {
}
