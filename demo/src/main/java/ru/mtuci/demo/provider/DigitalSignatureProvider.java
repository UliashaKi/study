package ru.mtuci.demo.provider;

import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class DigitalSignatureProvider {

    @Value("${jwt.secret}")
    private String secret;

    private SecretKeySpec secretKey;

    private static DigitalSignatureProvider digitalSignatureProvider;

    @PostConstruct
    private void init() {
        secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        digitalSignatureProvider = this;
    }

    public static DigitalSignatureProvider getInstance() {
        return digitalSignatureProvider;
    }

    public String sign(String input) {
        try {
            Mac sha256HMAC = Mac.getInstance("HmacSHA256");
            sha256HMAC.init(secretKey);
            return Base64.getEncoder().encodeToString(sha256HMAC.doFinal(input.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
