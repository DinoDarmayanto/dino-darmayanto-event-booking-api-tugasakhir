package com.sinaukoding.eventbooking.model.request;

import com.sinaukoding.eventbooking.model.enums.Role;
import com.sinaukoding.eventbooking.model.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterRequestRecord(
        @NotBlank(message = "Username wajib diisi")
        String username,

        @NotBlank(message = "Email wajib diisi")
        String email,

        @NotBlank(message = "Password wajib diisi")
        String password,

        @NotBlank(message = "First name wajib diisi")
        String firstName,

        @NotBlank(message = "Last name wajib diisi")
        String lastName,

        @NotBlank(message = "Nomor telepon wajib diisi")
        String phoneNumber,

        @NotNull(message = "Role wajib diisi")
        Role role,

        @NotNull(message = "Status wajib diisi")
        Status status
) {}
