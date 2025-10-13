package com.kindergarten.backend.dto;

import lombok.Data;

/**
 * Запрос на регистрацию нового пользователя
 */
@Data
public class RegisterRequest {
    private String username;  // Желаемое имя пользователя
    private String password;  // Пароль
}
