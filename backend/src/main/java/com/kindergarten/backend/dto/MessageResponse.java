package com.kindergarten.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Универсальный ответ с сообщением
 * Используется для уведомлений и ошибок
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {
    private String message;  // Текст сообщения
}