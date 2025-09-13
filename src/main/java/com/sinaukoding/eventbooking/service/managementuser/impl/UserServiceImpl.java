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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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

        if (userRepository.existsByEmail(request.email().toLowerCase())) {
            throw new RuntimeException("Email [" + request.email() + "] sudah digunakan");
        }
        if (userRepository.existsByUsername(request.username().toLowerCase())) {
            throw new RuntimeException("Username [" + request.username() + "] sudah digunakan");
        }

        User user = userMapper.requestToEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));

        userRepository.save(user);
    }

    @Override
    public void edit(UserRequestRecord request) {
        validatorService.validator(request);

        var userExisting = userRepository.findById(request.id())
                .orElseThrow(() -> new RuntimeException("Data user tidak ditemukan"));

        if (userRepository.existsByEmailAndIdNot(request.email().toLowerCase(), request.id())) {
            throw new RuntimeException("Email [" + request.email() + "] sudah digunakan");
        }
        if (userRepository.existsByUsernameAndIdNot(request.username().toLowerCase(), request.id())) {
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
    }

    @Override
    public SimpleMap delete(String id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Data user tidak ditemukan"));

        userRepository.delete(user);
        return userMapper.toSimpleMap(user, true);
    }

    @Override
    public AppPage<SimpleMap> findAll(UserFilterRequestRecord filterRequest, Pageable pageable) {
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
        return AppPage.create(mapped, pageable, page.getTotalElements());
    }




    @Override
    public SimpleMap findById(String id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Data user tidak ditemukan"));

        return userMapper.toSimpleMap(user, true);
    }
}
