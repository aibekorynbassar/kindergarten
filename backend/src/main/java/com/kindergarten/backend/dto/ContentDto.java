package com.kindergarten.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * DTO для передачи данных о контенте
 * Используется в API запросах/ответах
 */
@Data
public class ContentDto {
    private Long id;                    // ID новости
    private String title;               // Заголовок
    private String content;             // Содержание
    private String imageUrl;            // Ссылка на картинку
    private Boolean published;          // Статус публикации
    private LocalDateTime createdAt;    // Дата создания
    private LocalDateTime updatedAt;    // Дата изменения
}
