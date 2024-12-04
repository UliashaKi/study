package ru.mtuci.demo.model.dto;

public record CreateLicenseRequest(
        Long productId,
        Long ownerId,
        Long licenseTypeId,
        Integer duration,
        Integer deviceCount,
        String description) {
}
