package com.ninjaone.backendinterviewproject.resources.customer.database;

import com.ninjaone.backendinterviewproject.resources.customer.model.CustomerModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerModel, String> {
}
