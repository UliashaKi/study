package ru.mtuci.demo.model;

import java.time.LocalDateTime;

import lombok.Getter;
import ru.mtuci.demo.provider.DigitalSignatureProvider;

@Getter
public class Ticket {

    private final LocalDateTime serverDate;
    private final long lifetime;
    private final String email;
    private final String mac;
    private final long productId;
    private final LocalDateTime activationDate;
    private final LocalDateTime expirationDate;
    private final String digitalSignature;

    public Ticket(String email, String mac, long productId, LocalDateTime activationDate, LocalDateTime expirationDate) {
        this.serverDate = LocalDateTime.now();
        this.lifetime = 3600;
        this.email = email;
        this.mac = mac;
        this.productId = productId;
        this.activationDate = activationDate;
        this.expirationDate = expirationDate;
        this.digitalSignature = DigitalSignatureProvider.getInstance().sign(serverDate.toString() + lifetime + email + mac + productId + activationDate + expirationDate);
    }

}
