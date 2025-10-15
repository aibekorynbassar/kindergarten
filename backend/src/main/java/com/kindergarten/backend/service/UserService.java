package com.kindergarten.backend.service;

import com.kindergarten.backend.entity.Role;
import com.kindergarten.backend.entity.User;
import com.kindergarten.backend.repository.UserRepository;
import jakarta.annotation.PostConstruct;          // Выполняется после создания сервиса
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;  // Для шифрования паролей
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

/**
 * Сервис для работы с пользователями
 * Содержит бизнес-логику авторизации и управления пользователями
 */
@Service
@Transactional  // Все методы выполняются в транзакциях БД
public class UserService {

    @Autowired
    private UserRepository userRepository;  // Для работы с БД

    @Autowired
    private PasswordEncoder passwordEncoder;  // Для шифрования паролей

    /**
     * Создать нового пользователя
     * @param username имя пользователя
     * @param password пароль (будет зашифрован)
     * @param role роль пользователя
     * @return созданный пользователь
     */
    public User createUser(String username, String password, Role role) {
        // Проверяем, не существует ли уже такой пользователь
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));  // Шифруем пароль
        user.setRole(role);
        user.setEnabled(true);

        return userRepository.save(user);  // Сохраняем в БД
    }

    /**
     * Найти пользователя по имени
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Проверить пароль пользователя
     * @param rawPassword пароль в открытом виде
     * @param encodedPassword зашифрованный пароль из БД
     * @return true, если пароли совпадают
     */
    public boolean validatePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * Создает администратора по умолчанию при первом запуске
     * Выполняется автоматически после создания сервиса
     */
    @PostConstruct
    public void createDefaultAdmin() {
        // Если в БД нет пользователей - создаем админа
        if (userRepository.count() == 0) {
            createUser("admin", "admin123", Role.ADMIN);
            System.out.println("Default admin user created: admin/admin123");
        }
    }
}