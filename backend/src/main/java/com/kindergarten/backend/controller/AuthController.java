package com.kindergarten.backend.controller;

import com.kindergarten.backend.dto.MessageResponse;
import com.kindergarten.backend.dto.RegisterRequest;
import com.kindergarten.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;

/**
 * REST контроллер для авторизации
 * Обрабатывает запросы /api/auth/*
 */
@RestController                                      // Класс для REST API
@RequestMapping("/api/auth")                        // Базовый URL для всех методов
@CrossOrigin(origins = "http://localhost:5173")    // Разрешает запросы с фронтенда
public class AuthController {

    @Autowired
    private UserService userService;  // Сервис для работы с пользователями


    @GetMapping("/checker")
    public ResponseEntity<Map<String, Object>> checkAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        boolean authenticated = auth != null
                && auth.isAuthenticated()
                && !"anonymousUser".equals(auth.getPrincipal());

        if (authenticated) {
            boolean isAdmin = auth.getAuthorities().stream()
                    .anyMatch(granted -> granted.getAuthority().equals("ROLE_ADMIN"));
            return ResponseEntity.ok(Map.of(
                    "authenticated", true,
                    "role", isAdmin ? "ADMIN" : "USER"
            ));
        }
        return ResponseEntity.ok(Map.of("authenticated", false));
    }
    /**
     * Регистрация нового пользователя
     * POST /api/auth/register
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            // Создаем нового админа
            userService.createUser(request.getUsername(), request.getPassword(),
                    com.kindergarten.backend.entity.Role.ADMIN);
            return ResponseEntity.ok(new MessageResponse("User registered successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Registration failed: " + e.getMessage()));
        }
    }
}