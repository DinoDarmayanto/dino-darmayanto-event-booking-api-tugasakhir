package com.sinaukoding.eventbooking.model.filter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.validation.constraints.*;

public record EventFilterRequestRecord(
        @NotBlank(message = "Keyword tidak boleh kosong")
        String keyword,
        @NotBlank(message = "Category tidak boleh kosong")
        String category,
        @NotBlank(message = "Location tidak boleh kosong")
        String location,
        @NotNull(message = "Start date harus diisi")
        LocalDateTime startDate,
        @NotNull(message = "End date harus diisi")
        LocalDateTime endDate,
        @NotNull(message = "Min price harus diisi")
        BigDecimal minPrice,
        @NotNull(message = "Max price harus diisi")
        BigDecimal maxPrice,
        @NotNull(message = "Published harus diisi")
        Boolean published,
        @NotNull(message = "Page harus diisi")
        Integer page,
        @NotNull(message = "Size harus diisi")
        Integer size
) {}
