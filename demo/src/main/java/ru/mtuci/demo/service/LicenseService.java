package ru.mtuci.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.mtuci.demo.exception.NotFoundException;
import ru.mtuci.demo.model.entity.License;
import ru.mtuci.demo.repo.LicenseRepo;

@RequiredArgsConstructor
@Service
public class LicenseService {

  private final LicenseRepo licenseRepo;

  public License saveLicense(License license) {
    return licenseRepo.save(license);
  }

  public List<License> getAllLicenses() {
    return licenseRepo.findAll();
  }

  public License getLicenseById(long id) throws NotFoundException {
    return licenseRepo.findById(id).orElseThrow(() -> new NotFoundException("Лицензия с таким id не найдена"));
  }

  public License getLicenseByActivationCode(String activationCode) throws NotFoundException {
    return licenseRepo.findByActivationCode(activationCode).orElseThrow(() -> new NotFoundException("Лицензия с таким кодом активации не найдена"));
  }

  public List<License> getLicensesByOwnerId(long ownerId) {
    return licenseRepo.findByOwnerId(ownerId);
  }

  public List<License> getLicensesByUserId(long userId) {
    return licenseRepo.findByUserId(userId);
  }

  public void removeLicense(long id) {
    licenseRepo.deleteById(id);
  }
  
}
