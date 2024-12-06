package ru.mtuci.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.mtuci.demo.exception.EntityNotFoundException;
import ru.mtuci.demo.model.entity.User;
import ru.mtuci.demo.service.UserService;

@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class UserController {

  private final UserService userService;

  @PutMapping
  public ResponseEntity<?> saveUser(@RequestBody User user) {
    return ResponseEntity.ok(userService.saveUser(user));
  }

  @GetMapping
  public ResponseEntity<?> getAllUsers() {
    return ResponseEntity.ok(userService.getAllUsers());
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getUserById(@PathVariable long id) throws EntityNotFoundException {
    return ResponseEntity.ok(userService.getUserById(id));
  }

  @GetMapping("/login/{login}")
  public ResponseEntity<?> getUserByLogin(@PathVariable String login) throws EntityNotFoundException {
    return ResponseEntity.ok(userService.getUserByLogin(login));
  }

  @GetMapping("/email/{email}")
  public ResponseEntity<?> getUserByEmail(@PathVariable String email) throws EntityNotFoundException {
    return ResponseEntity.ok(userService.getUserByEmail(email));
  }

  @DeleteMapping("/{id}")
  public void removeUserById(@PathVariable long id) {
    userService.removeUserById(id);
  }
}
