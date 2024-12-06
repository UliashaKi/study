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
import ru.mtuci.demo.model.entity.LicenseType;
import ru.mtuci.demo.service.LicenseTypeService;

@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@RequestMapping("/license-types")
@RestController
public class LicenseTypeController {

  private final LicenseTypeService licenseTypeService;

  @PutMapping
  public ResponseEntity<?> saveLicenseType(@RequestBody LicenseType licenseType) {
    return ResponseEntity.ok(licenseTypeService.saveLicenseType(licenseType));
  }

  @GetMapping
  public ResponseEntity<?> getAllLicenseTypes() {
    return ResponseEntity.ok(licenseTypeService.getAllLicenseTypes());
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getLicenseTypeById(@PathVariable long id) throws NotFoundException {
    return ResponseEntity.ok(licenseTypeService.getLicenseTypeById(id));
  }

  @GetMapping("/name/{name}")
  public ResponseEntity<?> getLicenseTypeByName(@PathVariable String name) throws NotFoundException {
    return ResponseEntity.ok(licenseTypeService.getLicenseTypeByName(name));
  }

  @DeleteMapping("/{id}")
  public void removeLicenseTypeById(@PathVariable long id) {
    licenseTypeService.removeLicenseTypeById(id);
  }
}
