package ru.mtuci.demo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.mtuci.demo.exception.DemoException;
import ru.mtuci.demo.exception.NotFoundException;
import ru.mtuci.demo.model.Ticket;
import ru.mtuci.demo.model.dto.ActivateLicenseRequest;
import ru.mtuci.demo.model.dto.CreateLicenseRequest;
import ru.mtuci.demo.model.entity.DeviceLicense;
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
  private final DeviceService deviceService;
  private final DeviceLicenseService deviceLicenseService;

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
    return licenseRepo.findByActivationCode(activationCode)
        .orElseThrow(() -> new NotFoundException("Лицензия с таким кодом активации не найдена"));
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
    if (req.ownerId() == null) {
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

  public Ticket activateLicense(ActivateLicenseRequest req) throws DemoException {
    if (req.activationCode() == null) {
      throw new DemoException("Не указан код активации");
    }
    var device = deviceService.registerOrUpdateDevice(req.mac(), req.name());
    var license = getLicenseByActivationCode(req.activationCode());
    if (license.getBlocked()) {
      throw new DemoException("Лицензия заблокирована");
    }
    if (license.getProduct().getBlocked()) {
      throw new DemoException("Продукт заблокирован");
    }
    if (license.getUser() != null) {
      if (!license.getUser().getId().equals(device.getUser().getId())) {
        throw new DemoException("Лицензия принадлежит другому пользователю");
      }
      if (license.getActivationDate().plusDays(license.getDuration()).isBefore(LocalDateTime.now())) {
        throw new DemoException("Срок действия лицензии истек");
      }
    } else {
      license.setUser(device.getUser());
      license.setActivationDate(LocalDateTime.now());
      license = licenseRepo.save(license);
    }

    try {
      deviceLicenseService.getDeviceLicenseByDeviceIdAndLicenseId(device.getId(), license.getId());
      throw new DemoException("Устройство уже было активировано");
    } catch (NotFoundException e) {}

    if (license.getDeviceCount() <= license.getDeviceLicenses().size()) {
      throw new DemoException("Достигнут лимит устройств для данной лицензии");
    }

    var deviceLicense = new DeviceLicense();
    deviceLicense.setDevice(device);
    deviceLicense.setLicense(license);
    deviceLicense.setActivationDate(LocalDateTime.now());
    deviceLicense = deviceLicenseService.saveDeviceLicense(deviceLicense);

    licenseHistoryService.recordLicenseChange(license, "Активирована", "Лицензия активирована на устройстве " + device.getMac());

    return new Ticket(device.getUser().getEmail(), device.getMac(), license.getProduct().getId(), deviceLicense.getActivationDate(), license.getActivationDate().plusDays(license.getDuration()));
  }

  public List<Ticket> getLicenseInfo(String mac) throws DemoException {
    var device = deviceService.getDeviceByMac(mac);
    var currentUser = userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
    if (!device.getUser().getId().equals(currentUser.getId())) {
      throw new DemoException("Доступ запрещен");
    }
    List<Ticket> tickets = new ArrayList<>();
    for (var deviceLicense : device.getDeviceLicenses()) {
      var license = deviceLicense.getLicense();
      if (!(license.getBlocked() || license.getProduct().getBlocked() || license.getActivationDate().plusDays(license.getDuration()).isBefore(LocalDateTime.now()))) {
        tickets.add(new Ticket(device.getUser().getEmail(), device.getMac(), license.getProduct().getId(), deviceLicense.getActivationDate(), license.getActivationDate().plusDays(license.getDuration())));
      }
    }
    return tickets;
  }

}
