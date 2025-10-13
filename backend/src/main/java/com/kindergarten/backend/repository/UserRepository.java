package com.kindergarten.backend.repository;

import com.kindergarten.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Найти пользователя по имени
     * @param username имя пользователя
     * @return пользователь или пустой Optional
     */
    Optional<User> findByUsername(String username);

    /**
     * Проверить, существует ли пользователь с таким именем
     * @param username имя пользователя
     * @return true, если существует
     */
    boolean existsByUsername(String username);
}
