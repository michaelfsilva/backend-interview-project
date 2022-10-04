package com.ninjaone.backendinterviewproject.resources.device.database;

import com.ninjaone.backendinterviewproject.resources.device.model.DeviceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<DeviceModel, String> {
    List<DeviceModel> findByCustomerId(String id);

    boolean existsBySystemNameAndCustomerId(String systemName, String customerId);
}
