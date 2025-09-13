package com.sinaukoding.eventbooking.mapper.managementuser;

import com.sinaukoding.eventbooking.entity.managementuser.User;
import com.sinaukoding.eventbooking.model.app.SimpleMap;
import com.sinaukoding.eventbooking.model.enums.Role;
import com.sinaukoding.eventbooking.model.request.UserRequestRecord;
import org.springframework.stereotype.Component;

import java.util.Collections;


@Component
public class UserMapper {

    public User requestToEntity(UserRequestRecord request) {
        return User.builder()
                .username(request.username().toLowerCase())
                .email(request.email().toLowerCase())
                .password(request.password()) // nanti di-encode di service
                .firstName(request.firstName())
                .lastName(request.lastName())
                .phoneNumber(request.phoneNumber())
                .status(request.status())
                .role(Role.valueOf(request.role().toUpperCase())) // konversi String -> Enum
                .build();
    }

    /**
     * Mapping entity User -> SimpleMap
     *
     * @param user     data entity user
     * @param fullRole kalau true, return semua role (list), kalau false hanya ambil 1 role
     */
    public SimpleMap toSimpleMap(User user, boolean fullRole) {
        SimpleMap data = new SimpleMap();
        data.put("id", user.getId());
        data.put("nama",
                (user.getFirstName() == null ? "" : user.getFirstName()) + " " +
                        (user.getLastName() == null ? "" : user.getLastName()));
        data.put("username", user.getUsername());
        data.put("email", user.getEmail());
        data.put("status", user.getStatus() != null ? user.getStatus().name() : null);

        if (fullRole) {
            // bungkus ke list supaya konsisten, walau role cuma satu
            data.put("roles", user.getRole() == null
                    ? Collections.emptyList()
                    : Collections.singletonList(user.getRole().name()));
        } else {
            data.put("role", user.getRole() != null ? user.getRole().name() : null);
        }

        return data;
    }
}