package com.ninjaone.backendinterviewproject.application.controller;

import com.ninjaone.backendinterviewproject.application.dtos.ServiceDTO;
import com.ninjaone.backendinterviewproject.domain.service.services.ServiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/services")
public class ServiceController {
    @Autowired
    ServiceService serviceService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private ResponseEntity<ServiceDTO> save(@Valid @RequestBody ServiceDTO serviceDTO) {
        log.debug("Saving a new service: " + serviceDTO);
        try {
            return new ResponseEntity<>(serviceService.save(serviceDTO.toDomain()).toDTO(), HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            log.error("The service already exists in the database");
            return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void delete(@PathVariable String id) {
        log.debug("Deleting service: " + id);
        serviceService.delete(id);
    }
}
