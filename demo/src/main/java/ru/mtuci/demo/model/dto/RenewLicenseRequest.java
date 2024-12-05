package ru.mtuci.demo.model.dto;

public record RenewLicenseRequest(String activationCode, String mac) {
}
