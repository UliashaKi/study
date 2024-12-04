package ru.mtuci.demo.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.mtuci.demo.exception.DemoException;
import ru.mtuci.demo.exception.NotFoundException;
import ru.mtuci.demo.model.dto.CreateLicenseRequest;
import ru.mtuci.demo.model.entity.License;
import ru.mtuci.demo.repo.LicenseRepo;

@RequiredArgsConstructor
@Service
public class LicenseService {

  private final LicenseRepo licenseRepo;
  private final ProductService productService;
  private final UserService userService;
  private final LicenseTypeService licenseTypeService;
  private final LicenseHistoryService licenseHistoryService;

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

  public void removeLicenseById(long id) {
    licenseRepo.deleteById(id);
  }

  public License createLicense(CreateLicenseRequest req) throws DemoException {
    if (req.productId() == null) {
      throw new DemoException("Не указан продукт");
    }
    if (req.ownerId() == null){
      throw new DemoException("Не указан владелец");
    }
    if (req.licenseTypeId() == null) {
      throw new DemoException("Не указан тип лицензии");
    }
    var product = productService.getProductById(req.productId());
    if (product.getBlocked()) {
      throw new DemoException("Продукт заблокирован");
    }
    var owner = userService.getUserById(req.ownerId());
    var licenseType = licenseTypeService.getLicenseTypeById(req.licenseTypeId());
    var license = new License();
    license.setProduct(product);
    license.setOwner(owner);
    license.setLicenseType(licenseType);
    if (req.duration() != null) {
      license.setDuration(req.duration());
    } else {
      license.setDuration(licenseType.getDefaultDuration());
    }
    if (req.deviceCount() != null) {
      license.setDeviceCount(req.deviceCount());
    } else {
      license.setDeviceCount(1);
    }
    if (req.description() != null) {
      license.setDescription(req.description());
    } else {
      license.setDescription(licenseType.getDescription());
    }
    license.setActivationCode(UUID.randomUUID().toString());
    license.setBlocked(false);
    license = licenseRepo.save(license);
    licenseHistoryService.recordLicenseChange(license, "Создана", "Создана новая лицензия");
    return license;
  }

  
  
}
