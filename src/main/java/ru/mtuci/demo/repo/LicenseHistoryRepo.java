package ru.mtuci.demo.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.mtuci.demo.model.entity.LicenseHistory;

@Repository
public interface LicenseHistoryRepo extends JpaRepository<LicenseHistory, Long> {
    List<LicenseHistory> findByLicenseId(Long licenseId);
    List<LicenseHistory> findByUserId(Long userId);
}
