package com.sinaukoding.eventbooking.model.request;

import com.sinaukoding.eventbooking.model.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRequestRecord(
        @NotBlank(message = "ID wajib diisi")
        String id,
        @NotBlank(message = "Username wajib diisi")
        String username,
        @NotBlank(message = "Email wajib diisi")
        String email,
        String password, // optional untuk update
        @NotBlank(message = "First name wajib diisi")
        String firstName,
        @NotBlank(message = "Last name wajib diisi")
        String lastName,
        @NotBlank(message = "Nomor telepon wajib diisi")
        String phoneNumber,
        @NotNull(message = "Status wajib diisi")
        Status status,
        @NotBlank(message = "Role wajib diisi")
        String role
) {}
