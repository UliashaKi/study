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
import ru.mtuci.demo.exception.EntityNotFoundException;
import ru.mtuci.demo.model.entity.DeviceLicense;
import ru.mtuci.demo.service.DeviceLicenseService;

@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@RequestMapping("/device-licenses")
@RestController
public class DeviceLicenseController {

  private final DeviceLicenseService deviceLicenseService;

  @PutMapping
  public ResponseEntity<?> saveDeviceLicense(@RequestBody DeviceLicense deviceLicense) {
    return ResponseEntity.ok(deviceLicenseService.saveDeviceLicense(deviceLicense));
  }

  @GetMapping
  public ResponseEntity<?> getAllDeviceLicenses() {
    return ResponseEntity.ok(deviceLicenseService.getAllDeviceLicenses());
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getDeviceLicenseById(@PathVariable long id) throws EntityNotFoundException {
    return ResponseEntity.ok(deviceLicenseService.getDeviceLicenseById(id));
  }

  @GetMapping("/device/{deviceId}/license/{licenseId}")
  public ResponseEntity<?> getDeviceLicenseByDeviceIdAndLicenseId(@PathVariable long deviceId, @PathVariable long licenseId) throws EntityNotFoundException {
    return ResponseEntity.ok(deviceLicenseService.getDeviceLicenseByDeviceIdAndLicenseId(deviceId, licenseId));
  }

  @GetMapping("/device/{deviceId}")
  public ResponseEntity<?> getDeviceLicensesByDeviceId(@PathVariable long deviceId) {
    return ResponseEntity.ok(deviceLicenseService.getDeviceLicensesByDeviceId(deviceId));
  }

  @GetMapping("/license/{licenseId}")
  public ResponseEntity<?> getDeviceLicensesByLicenseId(@PathVariable long licenseId) {
    return ResponseEntity.ok(deviceLicenseService.getDeviceLicensesByLicenseId(licenseId));
  }

  @DeleteMapping("/{id}")
  public void removeDeviceLicenseById(@PathVariable long id) {
    deviceLicenseService.removeDeviceLicenseById(id);
  }
}
