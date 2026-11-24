package com.seckill.controller;

import com.seckill.common.ApiResponse;
import com.seckill.model.AuthRequest;
import com.seckill.model.LoginResult;
import com.seckill.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ApiResponse<Void> register(@RequestBody AuthRequest req) {
        authService.register(req.username(), req.password());
        return ApiResponse.ok();
    }

    @PostMapping("/login")
    public ApiResponse<LoginResult> login(@RequestBody AuthRequest req) {
        return ApiResponse.ok(authService.login(req.username(), req.password()));
    }
}
