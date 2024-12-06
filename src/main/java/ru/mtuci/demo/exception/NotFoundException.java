package ru.mtuci.demo.exception;

//TODO: 1. Не очень понятна суть ошибки. Лучше конкретизировать

public class NotFoundException extends DemoException {
    public NotFoundException(String message) {
        super(message);
    }
}
