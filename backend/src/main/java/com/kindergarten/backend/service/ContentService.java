package com.kindergarten.backend.service;

import com.kindergarten.backend.dto.ContentDto;
import com.kindergarten.backend.entity.Content;
import com.kindergarten.backend.repository.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ContentService {

    @Autowired
    private ContentRepository contentRepository;

    public List<ContentDto> getAllPublishedContent() {
        return contentRepository.findByPublished(true)
                .stream()
                .sorted(Comparator.comparing(Content::getCreatedAt).reversed())
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<ContentDto> getAllContent() {
        return contentRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Content::getCreatedAt).reversed())
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ContentDto createContent(ContentDto dto) {
        Content content = convertToEntity(dto);
        Content saved = contentRepository.save(content);
        return convertToDto(saved);
    }

    public ContentDto updateContent(Long id, ContentDto dto) {
        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Content not found"));
        content.setTitle(dto.getTitle());
        content.setContent(dto.getContent());
        content.setImageUrl(dto.getImageUrl());
        content.setPublished(dto.getPublished());
        Content updated = contentRepository.save(content);
        return convertToDto(updated);
    }

    public void deleteContent(Long id) {
        if (!contentRepository.existsById(id)) {
            throw new RuntimeException("Content not found");
        }
        contentRepository.deleteById(id);
    }

    private ContentDto convertToDto(Content content) {
        ContentDto dto = new ContentDto();
        dto.setId(content.getId());
        dto.setTitle(content.getTitle());
        dto.setContent(content.getContent());
        dto.setImageUrl(content.getImageUrl());
        dto.setPublished(content.getPublished());
        dto.setCreatedAt(content.getCreatedAt());
        return dto;
    }

    private Content convertToEntity(ContentDto dto) {
        Content content = new Content();
        content.setTitle(dto.getTitle());
        content.setContent(dto.getContent());
        content.setImageUrl(dto.getImageUrl());
        content.setPublished(dto.getPublished() != null ? dto.getPublished() : false);
        return content;
    }
}
