package com.carlosmurilo.tickethub.auth;

import com.carlosmurilo.tickethub.auth.dto.AuthResponse;
import com.carlosmurilo.tickethub.auth.dto.LoginRequest;
import com.carlosmurilo.tickethub.auth.dto.RegisterRequest;
import com.carlosmurilo.tickethub.user.User;
import com.carlosmurilo.tickethub.user.UserRepository;
import com.carlosmurilo.tickethub.user.UserRole;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest req) {
        String email = req.getEmail().trim().toLowerCase();

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email já está sendo usado");
        }

        User user = new User();
        user.setName(req.getName().trim());
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        user.setRole(UserRole.USER);

        User saved = userRepository.save(user);
        String token = jwtService.generateToken(saved);

        return new AuthResponse(token, saved.getId(), saved.getName(), saved.getEmail(), saved.getRole());
    }

    public AuthResponse login(LoginRequest req) {
        User user = userRepository.findByEmail(req.getEmail().trim().toLowerCase())
                .orElseThrow(() -> new BadCredentialsException("Email ou senha inválidos"));

        if (!passwordEncoder.matches(req.getPassword(), user.getPasswordHash())) {
            throw new BadCredentialsException("Email ou senha inválidos");
        }

        String token = jwtService.generateToken(user);

        return new AuthResponse(token, user.getId(), user.getName(), user.getEmail(), user.getRole());
    }
}