package ru.mtuci.demo.exception;

//TODO: 1. Не понятно, для чего эта кастомная ошибка создана

public class DemoException extends Exception {
  public DemoException(String message) {
    super(message);
  }
}
