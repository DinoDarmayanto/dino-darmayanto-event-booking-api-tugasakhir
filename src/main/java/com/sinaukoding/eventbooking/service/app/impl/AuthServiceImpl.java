package com.sinaukoding.eventbooking.service.app.impl;

import com.sinaukoding.eventbooking.entity.managementuser.User;
import com.sinaukoding.eventbooking.model.app.SimpleMap;
import com.sinaukoding.eventbooking.model.request.LoginRequestRecord;
import com.sinaukoding.eventbooking.model.request.RegisterRequestRecord;
import com.sinaukoding.eventbooking.repository.managementuser.UserRepository;
import com.sinaukoding.eventbooking.service.app.AuthService;
import com.sinaukoding.eventbooking.service.app.ValidatorService;
import com.sinaukoding.eventbooking.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final ValidatorService validatorService;

    @Override
    public SimpleMap register(RegisterRequestRecord request) {
        validatorService.validator(request);

        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email sudah terdaftar");
        }

        User user = User.builder()
                .username(request.username().toLowerCase())
                .email(request.email())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .phoneNumber(request.phoneNumber())
                .password(passwordEncoder.encode(request.password()))
                .role(request.role())      // dari request
                .status(request.status())  // dari request
                .build();

        userRepository.save(user);

        String token = jwtUtil.generateToken(
                Map.of("roles", Set.of(user.getRole().name())), user
        );

        return SimpleMap.createMap()
                .add("user", user)
                .add("token", token)
                .add("message", "Registrasi berhasil");
    }

    @Override
    public SimpleMap login(LoginRequestRecord request) {
        validatorService.validator(request);

        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new RuntimeException("Email tidak ditemukan"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new RuntimeException("Password salah");
        }

        Set<String> roles = Set.of(user.getRole().name());
        String token = jwtUtil.generateToken(Map.of("roles", roles), user);

        return SimpleMap.createMap()
                .add("user", user)
                .add("token", token)
                .add("roles", roles);
    }

    @Override
    public void logout(User userLoggedIn) {
        // Stateless JWT tidak menyimpan token di DB.
        // Logout cukup dilakukan di sisi client dengan menghapus token.
    }

}
