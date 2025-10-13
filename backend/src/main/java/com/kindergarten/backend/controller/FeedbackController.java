package com.kindergarten.backend.controller;

import com.kindergarten.backend.dto.FeedbackDto;
import com.kindergarten.backend.dto.MessageResponse;
import com.kindergarten.backend.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    // Создать новое обращение (от пользователя)
    @PostMapping("/feedback")
    public ResponseEntity<MessageResponse> createFeedback(@RequestBody FeedbackDto feedbackDto) {
        try {
            feedbackService.createFeedback(feedbackDto);
            return ResponseEntity.ok(new MessageResponse("Ваше сообщение успешно отправлено! Мы свяжемся с вами в ближайшее время."));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Ошибка при отправке сообщения: " + e.getMessage()));
        }
    }

    // Получить все обращения (для админа)
    @GetMapping("/admin/feedback")
    public ResponseEntity<List<FeedbackDto>> getAllFeedback() {
        try {
            List<FeedbackDto> feedback = feedbackService.getAllFeedback();
            return ResponseEntity.ok(feedback);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Удалить обращение (для админа)
    @DeleteMapping("/admin/feedback/{id}")
    public ResponseEntity<MessageResponse> deleteFeedback(@PathVariable Long id) {
        try {
            feedbackService.deleteFeedback(id);
            return ResponseEntity.ok(new MessageResponse("Обращение успешно удалено"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Ошибка при удалении: " + e.getMessage()));
        }
    }
}
