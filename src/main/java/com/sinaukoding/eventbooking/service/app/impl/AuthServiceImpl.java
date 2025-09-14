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
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final ValidatorService validatorService;

    @Override
    public SimpleMap register(RegisterRequestRecord request) {
        log.info("Memulai registrasi user dengan email: {}", request.email());
        try {
            validatorService.validator(request);

            if (userRepository.existsByEmail(request.email())) {
                log.warn("Registrasi gagal: Email {} sudah terdaftar", request.email());
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
            log.info("User {} berhasil disimpan di database", user.getEmail());

            String token = jwtUtil.generateToken(
                    Map.of("roles", Set.of(user.getRole().name())), user
            );
            log.info("Token JWT berhasil dibuat untuk user {}", user.getEmail());

            return SimpleMap.createMap()
                    .add("user", user)
                    .add("token", token)
                    .add("message", "Registrasi berhasil");
        } catch (Exception e) {
            log.error("Terjadi kesalahan saat registrasi user {}: {}", request.email(), e.getMessage());
            throw e;
        }
    }

    @Override
    public SimpleMap login(LoginRequestRecord request) {
        log.info("Memulai login untuk username: {}", request.username());
        try {
        validatorService.validator(request);

            User user = userRepository.findByUsername(request.username())
                    .orElseThrow(() -> {
                        log.warn("Login gagal: Username {} tidak ditemukan", request.username());
                        return new RuntimeException("Username tidak ditemukan");
                    });

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            log.warn("Login gagal: Password salah untuk username {}", request.username());
            throw new RuntimeException("Password salah");
        }

        Set<String> roles = Set.of(user.getRole().name());
        String token = jwtUtil.generateToken(Map.of("roles", roles), user);
            log.info("User {} berhasil login", request.username());


        return SimpleMap.createMap()
                .add("user", user)
                .add("token", token)
                .add("roles", roles);
    } catch (Exception e) {
            log.error("Terjadi kesalahan saat login user {}: {}", request.username(), e.getMessage());
            throw e;
        }
    }

    @Override
    public void logout(User userLoggedIn) {
        // Stateless JWT tidak menyimpan token di DB.
        // Logout cukup dilakukan di sisi client dengan menghapus token.
    }

}
