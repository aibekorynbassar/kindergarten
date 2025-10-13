package com.kindergarten.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

/**
 * Контент сайта (новости, объявления)
 * Хранится в таблице "content" в базе данных
 */
@Entity
@Table(name = "content")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)                        // Заголовок обязателен
    private String title;                            // Заголовок новости

    @Column(length = 2000)                          // Длинный текст до 2000 символов
    private String content;                         // Основной текст новости

    private String imageUrl;                        // Ссылка на изображение (необязательно)

    @Column(nullable = false)
    private Boolean published = false;              // Опубликована ли новость

    @CreationTimestamp                              // Автоматически при создании
    private LocalDateTime createdAt;

    @UpdateTimestamp                                // Автоматически при изменении
    private LocalDateTime updatedAt;
}
