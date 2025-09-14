package com.sinaukoding.eventbooking.model.request;

import jakarta.validation.constraints.NotBlank;


public record LoginRequestRecord(
        @NotBlank(message = "username wajib diisi")
        String username,

        @NotBlank(message = "Password wajib diisi")
        String password
) {}
