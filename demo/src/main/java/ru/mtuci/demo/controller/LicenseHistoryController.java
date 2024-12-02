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
import ru.mtuci.demo.model.entity.LicenseHistory;
import ru.mtuci.demo.service.LicenseHistoryService;

@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@RequestMapping("/license-histories")
@RestController
public class LicenseHistoryController {

  private final LicenseHistoryService licenseHistoryService;

  @PutMapping
  public ResponseEntity<?> saveLicenseHistory(@RequestBody LicenseHistory licenseHistory) {
    return ResponseEntity.ok(licenseHistoryService.saveLicenseHistory(licenseHistory));
  }

  @GetMapping
  public ResponseEntity<?> getAllLicenseHistories() {
    return ResponseEntity.ok(licenseHistoryService.getAllLicenseHistories());
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getLicenseHistoryById(@PathVariable long id) throws NotFoundException {
    return ResponseEntity.ok(licenseHistoryService.getLicenseHistoryById(id));
  }

  @GetMapping("/license/{licenseId}")
  public ResponseEntity<?> getLicenseHistoriesByLicenseId(@PathVariable long licenseId) {
    return ResponseEntity.ok(licenseHistoryService.getLicenseHistoriesByLicenseId(licenseId));
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<?> getLicenseHistoriesByUserId(@PathVariable long userId) {
    return ResponseEntity.ok(licenseHistoryService.getLicenseHistoriesByUserId(userId));
  }

  @DeleteMapping("/{id}")
  public void removeLicenseHistoryById(@PathVariable long id) {
    licenseHistoryService.removeLicenseHistoryById(id);
  }
}
