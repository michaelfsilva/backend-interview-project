package com.ninjaone.backendinterviewproject.application.controller;

import com.ninjaone.backendinterviewproject.application.dtos.DeviceDTO;
import com.ninjaone.backendinterviewproject.domain.device.entities.DeviceEntity;
import com.ninjaone.backendinterviewproject.domain.device.services.DeviceService;
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
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/devices")
public class DeviceController {
    @Autowired
    DeviceService deviceService;

    @GetMapping
    private List<DeviceDTO> getAll() {
        return deviceService.findAll().stream().map(DeviceEntity::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    private ResponseEntity<DeviceDTO> getById(@PathVariable String id) {
        try {
            return new ResponseEntity<>(
                    deviceService.findById(id).toDTO(),
                    HttpStatus.OK
            );
        } catch (NoSuchElementException e) {
            log.error("Device not found for id: " + id);
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private ResponseEntity<DeviceDTO> save(@Valid @RequestBody DeviceDTO deviceDTO) {
        log.debug("Saving a new device: " + deviceDTO);
        try {
            return new ResponseEntity<>(deviceService.save(deviceDTO.toDomain()).toDTO(), HttpStatus.CREATED);
        } catch (DataIntegrityViolationException | EntityNotFoundException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PutMapping("/{id}")
    private ResponseEntity<DeviceDTO> update(@PathVariable String id, @Valid @RequestBody DeviceDTO deviceDTO) {
        try {
            return new ResponseEntity<>(
                    deviceService.update(id, deviceDTO.toDomain()).toDTO(),
                    HttpStatus.OK
            );
        } catch (EntityNotFoundException e) {
            log.error("Device not found for id: " + id);
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void delete(@PathVariable String id) {
        deviceService.delete(id);
    }
}
