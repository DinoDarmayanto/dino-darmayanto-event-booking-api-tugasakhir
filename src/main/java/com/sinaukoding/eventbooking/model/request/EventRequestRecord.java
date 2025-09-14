package com.sinaukoding.eventbooking.model.request;

import com.sinaukoding.eventbooking.entity.managementuser.User;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record EventRequestRecord(
        @NotBlank(message = "ID wajib diisi")
        String id,

        @NotBlank(message = "Judul event wajib diisi")
        String title,

        @NotBlank(message = "Deskripsi event wajib diisi")
        String description,

        @NotNull(message = "Waktu mulai wajib diisi")
        @FutureOrPresent(message = "Waktu mulai harus hari ini atau di masa depan")
        LocalDateTime startTime,

        @NotNull(message = "Waktu selesai wajib diisi")
        @Future(message = "Waktu selesai harus di masa depan")
        LocalDateTime endTime,

        @NotNull(message = "Kapasitas wajib diisi")
        @Min(value = 1, message = "Kapasitas minimal 1")
        @Max(value = 100000, message = "Kapasitas maksimal 100000")
        Integer capacity,

        @NotNull(message = "Harga wajib diisi")
        @DecimalMin(value = "0.00", inclusive = true, message = "Harga minimal Rp0")
        @Digits(integer = 10, fraction = 2, message = "Harga maksimal 10 digit dengan 2 angka desimal")
        BigDecimal price,

        @NotBlank(message = "Lokasi wajib diisi")
        String location,

        @NotNull(message = "Status publish wajib diisi")
        Boolean published,

        @NotEmpty(message = "Minimal 1 kategori wajib dipilih")
        List<String> categories,

        @NotEmpty(message = "Minimal 1 tag wajib dipilih")
        List<String> tags,

        @NotBlank(message = "ID pembuat wajib diisi")
        String createdById
) {
}
