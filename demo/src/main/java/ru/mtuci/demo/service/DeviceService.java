package ru.mtuci.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.mtuci.demo.exception.NotFoundException;
import ru.mtuci.demo.model.entity.Device;
import ru.mtuci.demo.repo.DeviceRepo;

@RequiredArgsConstructor
@Service
public class DeviceService {

  private final DeviceRepo deviceRepo;

  public Device saveDevice(Device device) {
    return deviceRepo.save(device);
  }

  public List<Device> getAllDevices() {
    return deviceRepo.findAll();
  }

  public Device getDeviceById(long id) throws NotFoundException {
    return deviceRepo.findById(id).orElseThrow(() -> new NotFoundException("Устройство с таким id не найдено"));
  }

  public Device getDeviceByMac(String mac) throws NotFoundException {
    return deviceRepo.findByMac(mac).orElseThrow(() -> new NotFoundException("Устройство с таким mac-адресом не найдено"));
  }

  public List<Device> getDevicesByUserId(long userId) {
    return deviceRepo.findByUserId(userId);
  }

  public void removeDevice(long id) {
    deviceRepo.deleteById(id);
  }
  
}
