package com.sinaukoding.eventbooking.entity.event;

import com.sinaukoding.eventbooking.entity.app.BaseEntity;
import com.sinaukoding.eventbooking.entity.managementuser.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "t_event",
        indexes = {
                @Index(name = "idx_event_title", columnList = "title"),
                @Index(name = "idx_event_start_time", columnList = "start_time"),
                @Index(name = "idx_event_end_time", columnList = "end_time"),
                @Index(name = "idx_event_published", columnList = "published"),
                @Index(name = "idx_event_location", columnList = "location"),
                @Index(name = "idx_event_price", columnList = "price")
        }
)
public class Event extends BaseEntity {

    @Size(min = 5, max = 150, message = "Judul event harus antara 5–150 karakter")
    @Column(nullable = false, length = 150)
    private String title;

    @Size(min = 10, max = 2000, message = "Deskripsi event harus antara 10–2000 karakter")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @FutureOrPresent(message = "Waktu mulai harus hari ini atau di masa depan")
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Future(message = "Waktu selesai harus di masa depan")
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Min(value = 1, message = "Kapasitas minimal 1")
    @Max(value = 100000, message = "Kapasitas maksimal 100000")
    @Column(nullable = false)
    private Integer capacity;

    @DecimalMin(value = "0.00", inclusive = true, message = "Harga minimal Rp0")
    @Digits(integer = 10, fraction = 2, message = "Harga maksimal 10 digit dengan 2 angka desimal")
    @Column(precision = 12, scale = 2)
    private BigDecimal price;

    @NotBlank(message = "Lokasi tidak boleh kosong")
    @Size(max = 255, message = "Lokasi maksimal 255 karakter")
    @Column(nullable = false, length = 255)
    private String location;

    @Column(nullable = false)
    private boolean published = false;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "t_event_user",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"),
            uniqueConstraints = {
                    @UniqueConstraint(name = "uk_event_user", columnNames = {"event_id", "user_id"})
            }
    )
    private Set<User> createdBy = new HashSet<>();
}
