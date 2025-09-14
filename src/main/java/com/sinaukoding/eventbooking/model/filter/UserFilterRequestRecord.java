package com.sinaukoding.eventbooking.model.filter;

import com.sinaukoding.eventbooking.model.enums.Role;
import com.sinaukoding.eventbooking.model.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserFilterRequestRecord(
        @NotBlank(message = "Role tidak boleh kosong")
        Role role,
        @NotNull(message = "Status aktif harus diisi")
        Status active
) {}
