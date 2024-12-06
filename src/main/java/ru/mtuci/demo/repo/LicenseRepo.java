package ru.mtuci.demo.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.mtuci.demo.model.entity.License;

@Repository
public interface LicenseRepo extends JpaRepository<License, Long> {
    Optional<License> findByActivationCode(String activationCode);
    List<License> findByOwnerId(Long ownerId);
    List<License> findByUserId(Long userId);
}
