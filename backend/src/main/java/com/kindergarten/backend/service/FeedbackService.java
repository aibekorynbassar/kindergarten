package com.kindergarten.backend.service;

import com.kindergarten.backend.dto.FeedbackDto;
import com.kindergarten.backend.entity.Feedback;
import com.kindergarten.backend.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    public FeedbackDto createFeedback(FeedbackDto feedbackDto) {
        Feedback feedback = convertToEntity(feedbackDto);
        Feedback saved = feedbackRepository.save(feedback);
        return convertToDto(saved);
    }

    public List<FeedbackDto> getAllFeedback() {
        return feedbackRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public void deleteFeedback(Long id) {
        if (!feedbackRepository.existsById(id)) {
            throw new RuntimeException("Feedback not found");
        }
        feedbackRepository.deleteById(id);
    }

    private FeedbackDto convertToDto(Feedback feedback) {
        FeedbackDto dto = new FeedbackDto();
        dto.setId(feedback.getId());
        dto.setName(feedback.getName());
        dto.setPhone(feedback.getPhone());
        dto.setSubject(feedback.getSubject());
        dto.setMessage(feedback.getMessage());
        dto.setCreatedAt(feedback.getCreatedAt());
        return dto;
    }

    private Feedback convertToEntity(FeedbackDto dto) {
        Feedback feedback = new Feedback();
        feedback.setName(dto.getName());
        feedback.setPhone(dto.getPhone());
        feedback.setSubject(dto.getSubject());
        feedback.setMessage(dto.getMessage());
        return feedback;
    }
}