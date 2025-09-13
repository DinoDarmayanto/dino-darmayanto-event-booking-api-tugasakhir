package com.sinaukoding.eventbooking.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Slf4j
@Configuration
@EnableJpaAuditing
@RequiredArgsConstructor
public class AuditConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> {
            var authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                return Optional.of("SYSTEM"); // default system
            }

            Object principal = authentication.getPrincipal();

            if (principal instanceof UserLoggedInConfig userDetails) {
                return Optional.of(userDetails.getUser().getId()); // sudah String sekarang
            }

            return Optional.of(authentication.getName()); // fallback
        };
    }

}
