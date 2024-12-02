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
import ru.mtuci.demo.model.entity.License;
import ru.mtuci.demo.service.LicenseService;

@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@RequestMapping("/licenses")
@RestController
public class LicenseController {

  private final LicenseService licenseService;

  @PutMapping
  public ResponseEntity<?> saveLicense(@RequestBody License license) {
    return ResponseEntity.ok(licenseService.saveLicense(license));
  }

  @GetMapping
  public ResponseEntity<?> getAllLicenses() {
    return ResponseEntity.ok(licenseService.getAllLicenses());
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getLicenseById(@PathVariable long id) throws NotFoundException {
    return ResponseEntity.ok(licenseService.getLicenseById(id));
  }

  @GetMapping("/code/{activationCode}")
  public ResponseEntity<?> getLicenseByActivationCode(@PathVariable String activationCode) throws NotFoundException {
    return ResponseEntity.ok(licenseService.getLicenseByActivationCode(activationCode));
  }

  @GetMapping("/owner/{ownerId}")
  public ResponseEntity<?> getLicensesByOwnerId(@PathVariable long ownerId) {
    return ResponseEntity.ok(licenseService.getLicensesByOwnerId(ownerId));
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<?> getLicensesByUserId(@PathVariable long userId) {
    return ResponseEntity.ok(licenseService.getLicensesByUserId(userId));
  }

  @DeleteMapping("/{id}")
  public void removeLicenseById(@PathVariable long id) {
    licenseService.removeLicenseById(id);
  }

}
