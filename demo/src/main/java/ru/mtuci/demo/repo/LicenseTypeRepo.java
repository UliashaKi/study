package ru.mtuci.demo.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.mtuci.demo.model.entity.LicenseType;

@Repository
public interface LicenseTypeRepo extends JpaRepository<LicenseType, Long> {
    Optional<LicenseType> findByName(String name);
}
