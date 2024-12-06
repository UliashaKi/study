package ru.mtuci.demo.config;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import ru.mtuci.demo.exception.EntityAlreadyExistsException;
import ru.mtuci.demo.exception.APIException;

@RestControllerAdvice
public class DemoExceptionHandler {

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<String> handleNotFoundExceptions(NotFoundException ex) {
      return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(EntityAlreadyExistsException.class)
  public ResponseEntity<String> handleAlreadyExistsExceptions(EntityAlreadyExistsException ex) {
      return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
  }

  @ExceptionHandler(APIException.class)
  public ResponseEntity<String> handleDemoExceptions(APIException ex) {
      return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<String> handleAuthenticationExceptions(AuthenticationException ex) {
    return new ResponseEntity<>("Некорректные данные для аутентификации", HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleExceptions(Exception ex) {
    System.out.println(ex);
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
