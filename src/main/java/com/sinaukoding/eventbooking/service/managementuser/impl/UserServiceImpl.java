package com.sinaukoding.eventbooking.service.managementuser.impl;

import com.sinaukoding.eventbooking.builder.CustomBuilder;
import com.sinaukoding.eventbooking.entity.managementuser.User;
import com.sinaukoding.eventbooking.mapper.managementuser.UserMapper;
import com.sinaukoding.eventbooking.model.app.AppPage;
import com.sinaukoding.eventbooking.model.app.SimpleMap;
import com.sinaukoding.eventbooking.model.filter.UserFilterRequestRecord;
import com.sinaukoding.eventbooking.model.request.UserRequestRecord;
import com.sinaukoding.eventbooking.repository.managementuser.UserRepository;
import com.sinaukoding.eventbooking.service.app.ValidatorService;
import com.sinaukoding.eventbooking.service.managementuser.UserService;
import com.sinaukoding.eventbooking.util.FilterUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final ValidatorService validatorService;

    @Override
    public void add(UserRequestRecord request) {
        validatorService.validator(request);
        log.info("Menambahkan user baru dengan username [{}] dan email [{}]", request.username(), request.email());


        if (userRepository.existsByEmail(request.email().toLowerCase())) {
            log.warn("Email [{}] sudah digunakan", request.email());
            throw new RuntimeException("Email [" + request.email() + "] sudah digunakan");
        }
        if (userRepository.existsByUsername(request.username().toLowerCase())) {
            log.warn("Username [{}] sudah digunakan", request.username());
            throw new RuntimeException("Username [" + request.username() + "] sudah digunakan");
        }

        User user = userMapper.requestToEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));

        log.info("User [{}] berhasil disimpan", request.username());
        userRepository.save(user);
    }

    @Override
    public void edit(UserRequestRecord request) {
        validatorService.validator(request);
        log.info("Mengupdate user dengan ID [{}]", request.id());

        User userExisting = userRepository.findById(request.id())
                .orElseThrow(() -> {
                    log.warn("User dengan ID [{}] tidak ditemukan", request.id());
                    return new RuntimeException("Data user tidak ditemukan");
                });

        if (userRepository.existsByEmailAndIdNot(request.email().toLowerCase(), request.id())) {
            log.warn("Email [{}] sudah digunakan", request.email());
            throw new RuntimeException("Email [" + request.email() + "] sudah digunakan");
        }
        if (userRepository.existsByUsernameAndIdNot(request.username().toLowerCase(), request.id())) {
            log.warn("Username [{}] sudah digunakan", request.username());
            throw new RuntimeException("Username [" + request.username() + "] sudah digunakan");
        }


        User user = userMapper.requestToEntity(request);
        user.setId(userExisting.getId());

        // password hanya update kalau ada input baru
        if (request.password() != null && !request.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.password()));
        } else {
            user.setPassword(userExisting.getPassword());
        }

        userRepository.save(user);
        log.info("User dengan ID [{}] berhasil diupdate", request.id());
    }

    @Override
    public SimpleMap delete(String id) {
        log.info("Menghapus user dengan ID [{}]", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("User dengan ID [{}] tidak ditemukan", id);
                    return new RuntimeException("Data user tidak ditemukan");
                });

        userRepository.delete(user);
        log.info("User dengan ID [{}] berhasil dihapus", id);
        return userMapper.toSimpleMap(user, true);
    }

    @Override
    public AppPage<SimpleMap> findAll(UserFilterRequestRecord filterRequest, Pageable pageable) {
        log.info("Mencari semua user dengan filter: {}", filterRequest);
        CustomBuilder<User> builder = new CustomBuilder<>();

        if (filterRequest != null) {
            String keyword = "%%"; // placeholder untuk match semua
            FilterUtil.builderConditionNotBlankLike("firstName", keyword, builder);
            FilterUtil.builderConditionNotBlankLike("lastName", keyword, builder);
            FilterUtil.builderConditionNotBlankLike("username", keyword, builder);
            FilterUtil.builderConditionNotBlankLike("email", keyword, builder);

            if (filterRequest.role() != null) {
                FilterUtil.builderConditionNotNullEqual("role", filterRequest.role(), builder);
            }

            if (filterRequest.active() != null) {
                FilterUtil.builderConditionNotNullEqual("status", filterRequest.active(), builder);
            }
        }

        // Panggil repository dengan pageable (sudah membawa sorting)
        Page<User> page = userRepository.findAll(builder.build(), pageable);

        // Mapping User -> SimpleMap
        List<SimpleMap> mapped = page.getContent().stream()
                .map(user -> SimpleMap.createMap()
                        .add("id", user.getId())
                        .add("username", user.getUsername())
                        .add("email", user.getEmail())
                        .add("firstName", user.getFirstName())
                        .add("lastName", user.getLastName())
                        .add("role", user.getRole())
                        .add("status", user.getStatus())
                ).toList();

        // Return AppPage dengan total elements & pageable
        log.info("Ditemukan {} user", page.getTotalElements());
        return AppPage.create(mapped, pageable, page.getTotalElements());
    }




    @Override
    public SimpleMap findById(String id) {
        log.info("Mencari user dengan ID [{}]", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("User dengan ID [{}] tidak ditemukan", id);
                    return new RuntimeException("Data user tidak ditemukan");
                });
        log.info("User dengan ID [{}] ditemukan", id);
        return userMapper.toSimpleMap(user, true);
    }
}
