package ru.mtuci.demo.controller;

import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.mtuci.demo.exception.AlreadyExistsException;
import ru.mtuci.demo.exception.DemoException;
import ru.mtuci.demo.exception.NotFoundException;
import ru.mtuci.demo.model.Role;
import ru.mtuci.demo.model.dto.AuthRequest;
import ru.mtuci.demo.model.dto.AuthResponse;
import ru.mtuci.demo.model.entity.User;
import ru.mtuci.demo.provider.JWTProvider;
import ru.mtuci.demo.service.UserService;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {

    private final UserService userService;
    private final JWTProvider jwtProvider;

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest req) throws DemoException {
        if (req.login() == null || req.email() == null || req.password() == null) {
            throw new DemoException("Не все поля заполнены");
        }
        try {
            userService.getUserByLogin(req.login());
            throw new AlreadyExistsException("Пользователь с таким логином уже существует");
        } catch(NotFoundException e) {}

        try {
            userService.getUserByEmail(req.email());
            throw new AlreadyExistsException("Пользователь с такой почтой уже существует");
        } catch(NotFoundException e) {}
        
        var user = new User();
        user.setLogin(req.login());
        user.setPasswordHash(passwordEncoder.encode(req.password()));
        user.setEmail(req.email());
        user.setRole(Role.USER);
        userService.saveUser(user);

        return ResponseEntity.ok("Пользователь успешно зарегистрирован");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest req) throws NotFoundException {
        var login = req.login();
        var email = req.email();
        User user = null;
        if (login != null) {
            user = userService.getUserByLogin(login);
        }
        if (user == null && email != null) {
            user = userService.getUserByEmail(email);
        }
        if (user == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), req.password()));
        var token = jwtProvider.createToken(user.getEmail(), Set.of(new SimpleGrantedAuthority("ROLE_" + user.getRole())));

        return ResponseEntity.ok(new AuthResponse(user.getEmail(), token));
    }

}