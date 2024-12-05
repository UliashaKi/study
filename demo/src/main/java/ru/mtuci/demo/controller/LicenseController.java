package ru.mtuci.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.mtuci.demo.exception.DemoException;
import ru.mtuci.demo.exception.NotFoundException;
import ru.mtuci.demo.model.dto.ActivateLicenseRequest;
import ru.mtuci.demo.model.dto.CreateLicenseRequest;
import ru.mtuci.demo.model.dto.RenewLicenseRequest;
import ru.mtuci.demo.model.entity.License;
import ru.mtuci.demo.service.LicenseService;

@RequiredArgsConstructor
@RequestMapping("/licenses")
@RestController
public class LicenseController {

  private final LicenseService licenseService;

  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping
  public ResponseEntity<?> saveLicense(@RequestBody License license) {
    return ResponseEntity.ok(licenseService.saveLicense(license));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping
  public ResponseEntity<?> getAllLicenses() {
    return ResponseEntity.ok(licenseService.getAllLicenses());
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/{id}")
  public ResponseEntity<?> getLicenseById(@PathVariable long id) throws NotFoundException {
    return ResponseEntity.ok(licenseService.getLicenseById(id));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/code/{activationCode}")
  public ResponseEntity<?> getLicenseByActivationCode(@PathVariable String activationCode) throws NotFoundException {
    return ResponseEntity.ok(licenseService.getLicenseByActivationCode(activationCode));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/owner/{ownerId}")
  public ResponseEntity<?> getLicensesByOwnerId(@PathVariable long ownerId) {
    return ResponseEntity.ok(licenseService.getLicensesByOwnerId(ownerId));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/user/{userId}")
  public ResponseEntity<?> getLicensesByUserId(@PathVariable long userId) {
    return ResponseEntity.ok(licenseService.getLicensesByUserId(userId));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/{id}")
  public void removeLicenseById(@PathVariable long id) {
    licenseService.removeLicenseById(id);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping
  public ResponseEntity<?> createLicense(@RequestBody CreateLicenseRequest req) throws DemoException {
    return ResponseEntity.ok(licenseService.createLicense(req));
  }

  @PostMapping("/activate")
  public ResponseEntity<?> activateLicense(@RequestBody ActivateLicenseRequest req) throws DemoException {
    return ResponseEntity.ok(licenseService.activateLicense(req));
  }

  @GetMapping("/info/{mac}")
  public ResponseEntity<?> getLicenseInfo(@PathVariable String mac) throws DemoException {
    return ResponseEntity.ok(licenseService.getLicenseInfo(mac));
  }

  @PostMapping("/renew")
  public ResponseEntity<?> renewLicense(@RequestBody RenewLicenseRequest req) throws DemoException {
    return ResponseEntity.ok(licenseService.renewLicense(req));
  }

}
