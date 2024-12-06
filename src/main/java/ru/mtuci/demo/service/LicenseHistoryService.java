package ru.mtuci.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.mtuci.demo.exception.NotFoundException;
import ru.mtuci.demo.model.entity.License;
import ru.mtuci.demo.model.entity.LicenseHistory;
import ru.mtuci.demo.repo.LicenseHistoryRepo;

@RequiredArgsConstructor
@Service
public class LicenseHistoryService {

  private final LicenseHistoryRepo licenseHistoryRepo;
  private final UserService userService;

  public LicenseHistory saveLicenseHistory(LicenseHistory licenseHistory) {
    return licenseHistoryRepo.save(licenseHistory);
  }

  public List<LicenseHistory> getAllLicenseHistories() {
    return licenseHistoryRepo.findAll();
  }

  public LicenseHistory getLicenseHistoryById(long id) throws NotFoundException {
    return licenseHistoryRepo.findById(id).orElseThrow(() -> new NotFoundException("История лицензии с таким id не найдена"));
  }

  public List<LicenseHistory> getLicenseHistoriesByLicenseId(long licenseId) {
    return licenseHistoryRepo.findByLicenseId(licenseId);
  }

  public List<LicenseHistory> getLicenseHistoriesByUserId(long userId) {
    return licenseHistoryRepo.findByUserId(userId);
  }

  public void removeLicenseHistoryById(long id) {
    licenseHistoryRepo.deleteById(id);
  }

  public void recordLicenseChange(License license, String status, String description) throws NotFoundException {
    var licenseHistory = new LicenseHistory();
    licenseHistory.setLicense(license);
    licenseHistory.setUser(userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName()));
    licenseHistory.setStatus(status);
    licenseHistory.setDescription(description);
    licenseHistory.setChangeDate(LocalDateTime.now());
    licenseHistoryRepo.save(licenseHistory);
  }
  
}
