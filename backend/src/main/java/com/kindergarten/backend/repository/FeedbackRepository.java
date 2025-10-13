package com.kindergarten.backend.repository;

import com.kindergarten.backend.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    // Получить все обращения, отсортированные по дате (новые сперва)
    List<Feedback> findAllByOrderByCreatedAtDesc();
}