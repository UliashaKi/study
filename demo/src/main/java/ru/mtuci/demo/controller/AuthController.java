package ru.mtuci.demo.controller;

import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.mtuci.demo.exception.APIException;
import ru.mtuci.demo.model.dto.AuthRequest;
import ru.mtuci.demo.model.dto.AuthResponse;
import ru.mtuci.demo.provider.JWTProvider;
import ru.mtuci.demo.service.UserService;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {

    private final UserService userService;
    private final JWTProvider jwtProvider;

    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest req) throws APIException {
        userService.createUser(req);
        return ResponseEntity.ok("Пользователь успешно зарегистрирован");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest req) throws APIException {
        var login = req.login();
        var email = req.email();
        if (login == null && email == null) {
            throw new APIException("Не указан логин или email");
        }
        var user = userService.getUserByLoginOrEmail(login, email);
        var password = req.password();
        if (password == null) {
            throw new APIException("Не указан пароль");
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), password));
        return ResponseEntity.ok(new AuthResponse(user.getEmail(), jwtProvider.createToken(user.getEmail(), Set.of(new SimpleGrantedAuthority("ROLE_" + user.getRole())))));
    }

}