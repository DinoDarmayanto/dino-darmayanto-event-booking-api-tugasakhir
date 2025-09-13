package com.sinaukoding.eventbooking.controller.app;

import com.sinaukoding.eventbooking.config.UserLoggedInConfig;
import com.sinaukoding.eventbooking.model.request.LoginRequestRecord;
import com.sinaukoding.eventbooking.model.request.RegisterRequestRecord;
import com.sinaukoding.eventbooking.model.response.BaseResponse;
import com.sinaukoding.eventbooking.service.app.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "AUTH API")
public class AuthController {

    private final AuthService authService;

    @PostMapping("register")
    public BaseResponse<?> register(@RequestBody RegisterRequestRecord request) {
        return BaseResponse.ok("Registrasi berhasil", authService.register(request));
    }

    @PostMapping("login")
    public BaseResponse<?> login(@RequestBody LoginRequestRecord request) {
        return BaseResponse.ok("Login berhasil", authService.login(request));
    }

    @GetMapping("logout")
    public BaseResponse<?> logout(@AuthenticationPrincipal UserLoggedInConfig userLoggedInConfig) {
        var userLoggedIn = userLoggedInConfig.getUser();
        authService.logout(userLoggedIn);
        return BaseResponse.ok("Berhasil logout", null);
    }
}
