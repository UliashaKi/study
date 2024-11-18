package ru.mtuci.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.mtuci.demo.exception.NotFoundException;
import ru.mtuci.demo.model.entity.User;
import ru.mtuci.demo.repo.UserRepo;

@RequiredArgsConstructor
@Service
public class UserService {

  private final UserRepo userRepo;

  public User saveUser(User user) {
    return userRepo.save(user);
  }

  public List<User> getAllUsers() {
    return userRepo.findAll();
  }

  public User getUserById(long id) throws NotFoundException {
    return userRepo.findById(id).orElseThrow(() -> new NotFoundException("Пользователь с таким id не найден"));
  }

  public User getUserByLogin(String login) throws NotFoundException {
    return userRepo.findByLogin(login).orElseThrow(() -> new NotFoundException("Пользователь с таким логином не найден"));
  }

  public User getUserByEmail(String email) throws NotFoundException {
    return userRepo.findByEmail(email).orElseThrow(() -> new NotFoundException("Пользователь с таким email не найден"));
  }

  public void removeUser(long id) {
    userRepo.deleteById(id);
  }
  
}
