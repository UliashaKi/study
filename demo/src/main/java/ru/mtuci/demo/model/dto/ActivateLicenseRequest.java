package ru.mtuci.demo.model.dto;

public record ActivateLicenseRequest(String activationCode, String mac, String name) {
}
