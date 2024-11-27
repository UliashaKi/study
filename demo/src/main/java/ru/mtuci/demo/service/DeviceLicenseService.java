package ru.mtuci.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.mtuci.demo.exception.NotFoundException;
import ru.mtuci.demo.model.entity.DeviceLicense;
import ru.mtuci.demo.repo.DeviceLicenseRepo;

@RequiredArgsConstructor
@Service
public class DeviceLicenseService {

  private final DeviceLicenseRepo deviceLicenseRepo;

  public DeviceLicense saveDeviceLicense(DeviceLicense deviceLicense) {
    return deviceLicenseRepo.save(deviceLicense);
  }

  public List<DeviceLicense> getAllDeviceLicenses() {
    return deviceLicenseRepo.findAll();
  }

  public DeviceLicense getDeviceLicenseById(long id) throws NotFoundException {
    return deviceLicenseRepo.findById(id).orElseThrow(() -> new NotFoundException("Лицензия устройства с таким id не найдена"));
  }

  public DeviceLicense getDeviceLicenseByDeviceIdAndLicenseId(long deviceId, long licenseId) throws NotFoundException {
    return deviceLicenseRepo.findByDeviceIdAndLicenseId(deviceId, licenseId).orElseThrow(() -> new NotFoundException("Лицензия устройства с таким id и лицензией c таким id не найдена"));
  }

  public List<DeviceLicense> getDeviceLicensesByDeviceId(long deviceId) {
    return deviceLicenseRepo.findByDeviceId(deviceId);
  }

  public List<DeviceLicense> getDeviceLicensesByLicenseId(long licenseId) {
    return deviceLicenseRepo.findByLicenseId(licenseId);
  }

  public void removeDeviceLicense(long id) {
    deviceLicenseRepo.deleteById(id);
  }
  
}
