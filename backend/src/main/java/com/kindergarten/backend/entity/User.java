package com.kindergarten.backend.entity;

import jakarta.persistence.*;        // Аннотации для работы с БД
import lombok.AllArgsConstructor;    // Lombok: создает конструктор со всеми полями
import lombok.Data;                  // Lombok: создает геттеры/сеттеры/toString/equals/hashCode
import lombok.NoArgsConstructor;     // Lombok: создает пустой конструктор
import org.hibernate.annotations.CreationTimestamp;  // Автоматически ставит время создания
import java.time.LocalDateTime;

@Entity                              // Говорит Spring, что это таблица в БД
@Table(name = "users")              // Имя таблицы в БД
@Data                               // Lombok: автоматические геттеры/сеттеры
@NoArgsConstructor                  // Lombok: пустой конструктор (нужен для JPA)
@AllArgsConstructor                 // Lombok: конструктор со всеми полями
public class User {

    @Id                                              // Первичный ключ
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Автоинкремент в БД
    private Long id;

    @Column(unique = true, nullable = false)         // Уникальное имя пользователя
    private String username;                         // Логин для входа

    @Column(nullable = false)                        // Обязательное поле
    private String password;                         // Пароль (будет зашифрован)

    @Enumerated(EnumType.STRING)                     // Храним роль как строку в БД
    private Role role = Role.ADMIN;                  // По умолчанию - администратор

    @Column(nullable = false)
    private Boolean enabled = true;                  // Активен ли пользователь

    @CreationTimestamp                               // Автоматически заполняется при создании
    private LocalDateTime createdAt;                 // Когда создан пользователь
}