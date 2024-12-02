package ru.mtuci.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.mtuci.demo.exception.NotFoundException;
import ru.mtuci.demo.model.entity.LicenseType;
import ru.mtuci.demo.repo.LicenseTypeRepo;

@RequiredArgsConstructor
@Service
public class LicenseTypeService {

  private final LicenseTypeRepo licenseTypeRepo;

  public LicenseType saveLicenseType(LicenseType licenseType) {
    return licenseTypeRepo.save(licenseType);
  }

  public List<LicenseType> getAllLicenseTypes() {
    return licenseTypeRepo.findAll();
  }

  public LicenseType getLicenseTypeById(long id) throws NotFoundException {
    return licenseTypeRepo.findById(id).orElseThrow(() -> new NotFoundException("Тип лицензии с таким id не найден"));
  }

  public LicenseType getLicenseTypeByName(String name) throws NotFoundException {
    return licenseTypeRepo.findByName(name).orElseThrow(() -> new NotFoundException("Тип лицензии с таким именем не найден"));
  }

  public void removeLicenseTypeById(long id) {
    licenseTypeRepo.deleteById(id);
  }

}
