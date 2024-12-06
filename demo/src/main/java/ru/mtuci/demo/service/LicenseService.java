package ru.mtuci.demo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.mtuci.demo.exception.APIException;
import ru.mtuci.demo.exception.EntityNotFoundException;
import ru.mtuci.demo.model.Ticket;
import ru.mtuci.demo.model.dto.ActivateLicenseRequest;
import ru.mtuci.demo.model.dto.CreateLicenseRequest;
import ru.mtuci.demo.model.dto.RenewLicenseRequest;
import ru.mtuci.demo.model.entity.DeviceLicense;
import ru.mtuci.demo.model.entity.License;
import ru.mtuci.demo.model.entity.User;
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

  public License getLicenseById(long id) throws EntityNotFoundException {
    return licenseRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Лицензия с таким id не найдена"));
  }

  public License getLicenseByActivationCode(String activationCode) throws EntityNotFoundException {
    return licenseRepo.findByActivationCode(activationCode)
        .orElseThrow(() -> new EntityNotFoundException("Лицензия с таким кодом активации не найдена"));
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

  public License createLicense(CreateLicenseRequest req) throws APIException {
    if (req.productId() == null) {
      throw new APIException("Не указан продукт");
    }
    if (req.ownerId() == null) {
      throw new APIException("Не указан владелец");
    }
    if (req.licenseTypeId() == null) {
      throw new APIException("Не указан тип лицензии");
    }
    var product = productService.getProductById(req.productId());
    if (product.getBlocked()) {
      throw new APIException("Продукт заблокирован");
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
    do {
      license.setActivationCode(UUID.randomUUID().toString());
    } while (licenseRepo.findByActivationCode(license.getActivationCode()).isPresent());
    license.setBlocked(false);
    license = licenseRepo.save(license);
    licenseHistoryService.recordLicenseChange(license, "Создана", "Создана новая лицензия");
    return license;
  }

  public Ticket activateLicense(ActivateLicenseRequest req) throws APIException {
    if (req.activationCode() == null) {
      throw new APIException("Не указан код активации");
    }
    var device = deviceService.registerOrUpdateDevice(req.mac(), req.name());
    var license = getLicenseByActivationCode(req.activationCode());
    checkLicenseBlock(license);
    if (license.getUser() != null) {
      if (!license.getUser().getId().equals(device.getUser().getId())) {
        throw new APIException("Лицензия принадлежит другому пользователю");
      }
      checkLicenseExpiration(license);
    } else {
      license.setUser(device.getUser());
      license.setActivationDate(LocalDateTime.now());
      license = licenseRepo.save(license);
    }

    try {
      deviceLicenseService.getDeviceLicenseByDeviceIdAndLicenseId(device.getId(), license.getId());
      throw new APIException("Устройство уже было активировано");
    } catch (EntityNotFoundException e) {
      if (license.getDeviceCount() <= license.getDeviceLicenses().size()) {
        throw new APIException("Достигнут лимит устройств для данной лицензии");
      }

      var deviceLicense = new DeviceLicense();
      deviceLicense.setDevice(device);
      deviceLicense.setLicense(license);
      deviceLicense.setActivationDate(LocalDateTime.now());
      deviceLicense = deviceLicenseService.saveDeviceLicense(deviceLicense);

      licenseHistoryService.recordLicenseChange(license, "Активирована", "Лицензия активирована на устройстве " + device.getMac());

      return createTicket(deviceLicense);
  }
  }

  public List<Ticket> getLicenseInfo(String mac) throws APIException {
    var device = deviceService.getDeviceByMac(mac);
    var currentUser = getCurrentUser();
    if (!device.getUser().getId().equals(currentUser.getId())) {
      throw new APIException("Доступ запрещен");
    }
    List<Ticket> tickets = new ArrayList<>();
    for (var deviceLicense : device.getDeviceLicenses()) {
      var license = deviceLicense.getLicense();
      if (!(license.getBlocked() || license.getProduct().getBlocked() || license.getActivationDate().plusDays(license.getDuration()).isBefore(LocalDateTime.now()))) {
        tickets.add(createTicket(deviceLicense));
      }
    }
    return tickets;
  }

  public Ticket renewLicense(RenewLicenseRequest req) throws APIException {
    var device = deviceService.getDeviceByMac(req.mac());
    var license = getLicenseByActivationCode(req.activationCode());
    checkLicenseBlock(license);
    if (license.getUser() == null) {
      throw new APIException("Лицензия не активирована");
    }
    var currentUser = getCurrentUser();
    if (!license.getUser().getId().equals(currentUser.getId()) || !device.getUser().getId().equals(currentUser.getId())) {
      throw new APIException("Доступ запрещен");
    }
    checkLicenseExpiration(license);
    license.setDuration(license.getDuration() + license.getLicenseType().getDefaultDuration());
    license = licenseRepo.save(license);
    licenseHistoryService.recordLicenseChange(license, "Продлена", "Лицензия продлена");

    var deviceLicense = deviceLicenseService.getDeviceLicenseByDeviceIdAndLicenseId(device.getId(), license.getId());

    return createTicket(deviceLicense);
  }

  private Ticket createTicket(DeviceLicense deviceLicense) {
    var license = deviceLicense.getLicense();
    var device = deviceLicense.getDevice();
    return new Ticket(license.getUser().getEmail(), device.getMac(), license.getProduct().getId(), license.getId(), license.getActivationDate(), license.getActivationDate().plusDays(license.getDuration()));
  }

  private void checkLicenseBlock(License license) throws APIException {
    if (license.getBlocked()) {
      throw new APIException("Лицензия заблокирована");
    }
    if (license.getProduct().getBlocked()) {
      throw new APIException("Продукт заблокирован");
    }
  }

  private void checkLicenseExpiration(License license) throws APIException {
    if (license.getActivationDate().plusDays(license.getDuration()).isBefore(LocalDateTime.now())) {
      throw new APIException("Срок действия лицензии истек");
    }
  }

  private User getCurrentUser() throws EntityNotFoundException {
    return userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
  }

}
