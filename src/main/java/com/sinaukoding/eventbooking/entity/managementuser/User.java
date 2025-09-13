package com.sinaukoding.eventbooking.entity.managementuser;

import com.sinaukoding.eventbooking.entity.app.BaseEntity;
import com.sinaukoding.eventbooking.entity.event.Event;
import com.sinaukoding.eventbooking.model.enums.Role;
import com.sinaukoding.eventbooking.model.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;


@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "m_user",
        indexes = {
                @Index(name = "idx_user_username", columnList = "username"),
                @Index(name = "idx_user_email", columnList = "email"),
                @Index(name = "idx_user_first_name", columnList = "first_name"),
                @Index(name = "idx_user_last_name", columnList = "last_name"),
                @Index(name = "idx_user_phone_number", columnList = "phone_number"),
                @Index(name = "idx_user_status", columnList = "status"),
        }
)
public class User extends BaseEntity {

    @Size(min = 4, max = 50, message = "Username harus antara 4–50 karakter")
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Email(message = "Format email tidak valid")
    @Size(max = 100, message = "Email maksimal 100 karakter")
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Size(min = 8, max = 255, message = "Password minimal 8 karakter")
    @Column(nullable = false, length = 255)
    private String password;

    @Size(max = 100, message = "First name maksimal 100 karakter")
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Size(max = 100, message = "Last name maksimal 100 karakter")
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Pattern(
            regexp = "^(\\+62|62|0)8[1-9][0-9]{6,10}$",
            message = "Nomor telepon harus format Indonesia yang valid"
    )
    @Size(max = 15, message = "Nomor telepon maksimal 15 digit")
    @Column(name = "phone_number", nullable = false, unique = true, length = 15)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role = Role.USER;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status = Status.AKTIF;

    @Future(message = "Waktu expired token harus di masa depan")
    @Column(name = "expired_token_at")
    private LocalDateTime expiredTokenAt;

}
