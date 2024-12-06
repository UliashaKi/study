package ru.mtuci.demo.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.mtuci.demo.model.entity.Device;

@Repository
public interface DeviceRepo extends JpaRepository<Device, Long> {
    Optional<Device> findByMac(String mac);
    List<Device> findByUserId(Long userId);
}
