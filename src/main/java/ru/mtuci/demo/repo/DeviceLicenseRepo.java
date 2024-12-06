package ru.mtuci.demo.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.mtuci.demo.model.entity.DeviceLicense;

@Repository
public interface DeviceLicenseRepo extends JpaRepository<DeviceLicense, Long> {
    Optional<DeviceLicense> findByDeviceIdAndLicenseId(Long deviceId, Long licenseId);
    List<DeviceLicense> findByDeviceId(Long deviceId);
    List<DeviceLicense> findByLicenseId(Long licenseId);
}
