package com.carlosmurilo.tickethub.user;

import com.carlosmurilo.tickethub.user.dto.CreateUserRequest;
import com.carlosmurilo.tickethub.user.dto.UserResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse create(CreateUserRequest req) {
        String email = req.getEmail().trim().toLowerCase();

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already in use");
        }

        User user = new User();
        user.setName(req.getName().trim());
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(req.getPassword()));

        User saved = userRepository.save(user);

        return new UserResponse(
                saved.getId(),
                saved.getName(),
                saved.getEmail(),
                saved.getCreatedAt()
        );
    }
}