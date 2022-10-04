package com.ninjaone.backendinterviewproject.application.controller;

import com.ninjaone.backendinterviewproject.application.dtos.CustomerDTO;
import com.ninjaone.backendinterviewproject.domain.customer.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequestMapping("/customers")
public class CustomerController {
    @Autowired
    CustomerService customerService;

    @GetMapping("/{id}")
    private ResponseEntity<CustomerDTO> getById(@PathVariable String id) {
        try {
            return new ResponseEntity<>(
                    customerService.findById(id).toDTO(),
                    HttpStatus.OK
            );
        } catch (NoSuchElementException e) {
            log.error("Customer not found for id: " + id);
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private ResponseEntity<CustomerDTO> save(@Valid @RequestBody CustomerDTO customerDTO) {
        log.debug("Saving a new customer: " + customerDTO);
        try {
            return new ResponseEntity<>(customerService.save(customerDTO.toDomain()).toDTO(), HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            log.error("The customer already exists in the database");
            return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PutMapping("/{id}")
    private ResponseEntity<CustomerDTO> update(@PathVariable String id, @Valid @RequestBody CustomerDTO customerDTO) {
        try {
            return new ResponseEntity<>(
                    customerService.update(id, customerDTO.toDomain()).toDTO(),
                    HttpStatus.OK
            );
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void delete(@PathVariable String id) {
        log.debug("Deleting customer: " + id);
        customerService.delete(id);
    }

    @GetMapping("/{id}/costs")
    private ResponseEntity<BigDecimal> calculateMonthlyCosts(@PathVariable String id) {
        log.debug("Calculating costs for customer: " + id);
        try {
            return new ResponseEntity<>(
                    customerService.getMonthlyCostsById(id),
                    HttpStatus.OK
            );
        } catch (EntityNotFoundException e) {
            log.error("Customer not found for id: " + id);
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
