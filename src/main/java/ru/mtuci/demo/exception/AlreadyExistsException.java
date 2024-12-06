package ru.mtuci.demo.exception;

//TODO: 1. Не очень понятна суть ошибки. Лучше конкретизировать

public class AlreadyExistsException extends DemoException {
    public AlreadyExistsException(String message) {
        super(message);
    }
}
