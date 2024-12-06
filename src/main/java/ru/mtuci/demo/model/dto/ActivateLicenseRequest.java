package ru.mtuci.demo.model.dto;

//TODO: 1. Очень интересно это обсудить с вами

public record ActivateLicenseRequest(String activationCode, String mac, String name) {
}
