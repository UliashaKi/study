package ru.mtuci.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.mtuci.demo.exception.NotFoundException;
import ru.mtuci.demo.model.entity.Device;
import ru.mtuci.demo.service.DeviceService;

@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@RequestMapping("/devices")
@RestController
public class DeviceController {

  private final DeviceService deviceService;

  @PutMapping
  public ResponseEntity<?> saveDevice(@RequestBody Device device) {
   return ResponseEntity.ok(deviceService.saveDevice(device));
  }

  @GetMapping
  public ResponseEntity<?> getAllDevices() {
    return ResponseEntity.ok(deviceService.getAllDevices());
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getDeviceById(@PathVariable long id) throws NotFoundException {
    return ResponseEntity.ok(deviceService.getDeviceById(id));
  }

  @GetMapping("/mac/{mac}")
  public ResponseEntity<?> getDeviceByMac(@PathVariable String mac) throws NotFoundException {
    return ResponseEntity.ok(deviceService.getDeviceByMac(mac));
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<?> getDevicesByUserId(@PathVariable long userId) {
    return ResponseEntity.ok(deviceService.getDevicesByUserId(userId));
  }

  @DeleteMapping("/{id}")
  public void removeDeviceById(@PathVariable long id) {
    deviceService.removeDeviceById(id);
  }

}
