package ru.mtuci.demo.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.mtuci.demo.exception.EntityAlreadyExistsException;
import ru.mtuci.demo.exception.APIException;
import ru.mtuci.demo.exception.EntityNotFoundException;
import ru.mtuci.demo.model.Role;
import ru.mtuci.demo.model.dto.AuthRequest;
import ru.mtuci.demo.model.entity.User;
import ru.mtuci.demo.repo.UserRepo;

@RequiredArgsConstructor
@Service
public class UserService {

  private final UserRepo userRepo;

  private final PasswordEncoder passwordEncoder;

  public User saveUser(User user) {
    return userRepo.save(user);
  }

  public List<User> getAllUsers() {
    return userRepo.findAll();
  }

  public User getUserById(long id) throws EntityNotFoundException {
    return userRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Пользователь с таким id не найден"));
  }

  public User getUserByLogin(String login) throws EntityNotFoundException {
    return userRepo.findByLogin(login).orElseThrow(() -> new EntityNotFoundException("Пользователь с таким логином не найден"));
  }

  public User getUserByEmail(String email) throws EntityNotFoundException {
    return userRepo.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Пользователь с таким email не найден"));
  }

  public void removeUserById(long id) {
    userRepo.deleteById(id);
  }

  public void createUser(AuthRequest req) throws APIException {
        if (req.login() == null) {
            throw new APIException("Не указан логин");
        }
        if (req.email() == null) {
            throw new APIException("Не указан email");
        }
        if (req.password() == null) {
            throw new APIException("Не указан пароль");
        }
        if (userRepo.existsByLogin(req.login())) {
            throw new EntityAlreadyExistsException("Пользователь с таким логином уже существует");
        }
        if (userRepo.existsByEmail(req.email())) {
            throw new EntityAlreadyExistsException("Пользователь с такой почтой уже существует");
        }
        var user = new User();
        user.setLogin(req.login());
        user.setPasswordHash(passwordEncoder.encode(req.password()));
        user.setEmail(req.email());
        user.setRole(Role.USER);
        userRepo.save(user);
  }

  public User getUserByLoginOrEmail(String login, String email) throws EntityNotFoundException {
    return userRepo.findByLoginOrEmail(login, email).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
  }
  
}
