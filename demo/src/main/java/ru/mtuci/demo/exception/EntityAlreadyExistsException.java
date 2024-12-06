package ru.mtuci.demo.exception;

public class EntityAlreadyExistsException extends APIException {
    public EntityAlreadyExistsException(String message) {
        super(message);
    }
}
