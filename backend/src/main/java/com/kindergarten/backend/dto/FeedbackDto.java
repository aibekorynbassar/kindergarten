package com.kindergarten.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackDto {
    private Long id;
    private String name;
    private String phone;
    private String subject;
    private String message;
    private LocalDateTime createdAt;
}