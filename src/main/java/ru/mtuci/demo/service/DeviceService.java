package ru.mtuci.demo.service;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.mtuci.demo.exception.DemoException;
import ru.mtuci.demo.exception.NotFoundException;
import ru.mtuci.demo.model.entity.Device;
import ru.mtuci.demo.repo.DeviceRepo;

@RequiredArgsConstructor
@Service
public class DeviceService {

  private final DeviceRepo deviceRepo;
  private final UserService userService;

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

  public void removeDeviceById(long id) {
    deviceRepo.deleteById(id);
  }

  public Device registerOrUpdateDevice(String mac, String name) throws DemoException {
    if (mac == null) {
      throw new DemoException("Не указан mac-адрес");
    }
    var currentUser = userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
    var device = deviceRepo.findByMac(mac);
    if (device.isPresent()) {
      var existingDevice = device.get();
      if (!existingDevice.getUser().getId().equals(currentUser.getId())) {
        throw new DemoException("Устройство с таким mac-адресом уже зарегистрировано другим пользователем");
      }
      if (name != null) {
        existingDevice.setName(name);
      }
      return deviceRepo.save(existingDevice);
    }
    if (name == null) {
      throw new DemoException("Не указано название устройства");
    }
    var newDevice = new Device();
    newDevice.setMac(mac);
    newDevice.setName(name);
    newDevice.setUser(currentUser);
    return deviceRepo.save(newDevice);

  }
  
}
