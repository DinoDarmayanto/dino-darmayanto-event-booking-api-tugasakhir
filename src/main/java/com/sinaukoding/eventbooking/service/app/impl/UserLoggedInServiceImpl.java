package com.sinaukoding.eventbooking.service.app.impl;

import com.sinaukoding.eventbooking.config.UserLoggedInConfig;
import com.sinaukoding.eventbooking.model.app.Checks;
import com.sinaukoding.eventbooking.repository.managementuser.UserRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserLoggedInServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Mencari user dengan username/email '{}'", username);

        var user = userRepository.findByUsername(username)
                .or(() -> userRepository.findByEmail(username))
                .orElseThrow(() -> {
                    log.warn("User dengan username/email '{}' tidak ditemukan", username);
                    return new UsernameNotFoundException(
                            "User dengan username/email '" + username + "' tidak ditemukan");
                });
        log.info("User '{}' berhasil ditemukan", username);
        return new UserLoggedInConfig(user);
    }
}
