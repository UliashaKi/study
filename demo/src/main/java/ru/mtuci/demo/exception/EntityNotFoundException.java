package ru.mtuci.demo.exception;

public class EntityNotFoundException extends APIException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
