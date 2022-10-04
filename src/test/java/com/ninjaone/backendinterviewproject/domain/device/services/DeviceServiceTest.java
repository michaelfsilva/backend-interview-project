package com.ninjaone.backendinterviewproject.domain.device.services;

import com.ninjaone.backendinterviewproject.domain.device.entities.DeviceEntity;
import com.ninjaone.backendinterviewproject.resources.customer.database.CustomerRepository;
import com.ninjaone.backendinterviewproject.resources.customer.model.CustomerModel;
import com.ninjaone.backendinterviewproject.resources.device.database.DeviceRepository;
import com.ninjaone.backendinterviewproject.resources.device.model.DeviceModel;
import com.ninjaone.backendinterviewproject.resources.service.model.ServiceModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.ninjaone.backendinterviewproject.domain.device.entities.enums.DeviceType.WINDOWS_WORKSTATION;
import static com.ninjaone.backendinterviewproject.domain.service.entities.enums.ServiceType.ANTIVIRUS_WINDOWS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeviceServiceTest {
    public static final String ID = "12345";

    @Mock
    private DeviceRepository deviceRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private DeviceService deviceService;

    private DeviceModel deviceModel;
    private CustomerModel customerModel;

    @BeforeEach
    void setup() {
        customerModel = new CustomerModel(ID, "Test Customer");
        deviceModel = new DeviceModel(
                ID,
                "Windows Workstation",
                WINDOWS_WORKSTATION.name(),
                List.of(ServiceModel.builder().id("1").type(ANTIVIRUS_WINDOWS.name()).build()),
                customerModel);
    }

    @Test
    void shouldGetAllDevices() throws Exception {
        List<DeviceModel> devices = Arrays.asList(deviceModel, deviceModel, deviceModel);

        when(deviceRepository.findAll()).thenReturn(devices);

        List<DeviceEntity> deviceEntityList = deviceService.findAll();

        assertNotEquals(0, deviceEntityList.size());
        assertEquals(deviceModel.getSystemName(), deviceEntityList.get(0).getSystemName());
        assertEquals(deviceModel.getType(), deviceEntityList.get(0).getType().name());
    }

    @Test
    void shouldGetDeviceById() throws Exception {
        when(deviceRepository.findById(ID)).thenReturn(Optional.of(deviceModel));

        DeviceEntity device = deviceService.findById(ID);

        assertNotNull(device);
        assertEquals(deviceModel.getSystemName(), device.getSystemName());
        assertEquals(deviceModel.getType(), device.getType().name());
    }

    @Test
    void shouldThrowExceptionWhenGetDeviceByIdAndDeviceIdIsNotFound() throws Exception {
        when(deviceRepository.findById(ID)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> deviceService.findById(ID));
    }

    @Test
    void shouldCreateADevice() throws Exception {
        when(customerRepository.findById(any())).thenReturn(Optional.of(customerModel));
        when(deviceRepository.save(any())).thenReturn(deviceModel);

        DeviceEntity savedDevice = deviceService.save(deviceModel.toDomain());

        assertEquals(deviceModel.getSystemName(), savedDevice.getSystemName());
        assertEquals(deviceModel.getType(), savedDevice.getType().name());
        assertEquals(deviceModel.getCustomer().getId(), savedDevice.getCustomerId());
    }

    @Test
    void shouldUpdateADevice() throws Exception {
        var updatedDevice = deviceModel;
        updatedDevice.setSystemName("New System Name");

        when(customerRepository.findById(any())).thenReturn(Optional.of(customerModel));
        when(deviceRepository.findById(ID)).thenReturn(Optional.of(deviceModel));
        when(deviceRepository.save(any())).thenReturn(updatedDevice);

        DeviceEntity savedDevice = deviceService.update(ID, updatedDevice.toDomain());

        assertEquals(deviceModel.getSystemName(), savedDevice.getSystemName());
        assertEquals(deviceModel.getType(), savedDevice.getType().name());
        assertEquals(deviceModel.getCustomer().getId(), savedDevice.getCustomerId());
    }

    @Test
    void shouldThrowExceptionWhenUpdateDeviceAndDeviceIdIsNotFound() throws Exception {
        when(customerRepository.findById(any())).thenReturn(Optional.of(customerModel));
        when(deviceRepository.findById(ID)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> deviceService.update(ID, deviceModel.toDomain()));
    }

    @Test
    void shouldDeleteADevice() throws Exception {
        doNothing().when(deviceRepository).deleteById(ID);
        deviceService.delete(ID);
        Mockito.verify(deviceRepository, times(1)).deleteById(ID);
    }
}
