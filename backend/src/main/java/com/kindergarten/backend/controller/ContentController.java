package com.kindergarten.backend.controller;

import com.kindergarten.backend.dto.ContentDto;
import com.kindergarten.backend.dto.MessageResponse;
import com.kindergarten.backend.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class ContentController {
    @Autowired
    private ContentService contentService;

//    @GetMapping("/news")
//    public ResponseEntity<List<ContentDto>> getAllNews(){
//        List<ContentDto> news = contentService.getAllContent();
//        return ResponseEntity.ok(news);
//    }

    @GetMapping("/published_news")
    public ResponseEntity<List<ContentDto>> getOnlyPublishedNews(){
        List<ContentDto> news = contentService.getAllPublishedContent();
        return ResponseEntity.ok(news);
    }

    @GetMapping("/admin/news")
    public ResponseEntity<List<ContentDto>> getAllNewsForAdmin(){
        List<ContentDto> news = contentService.getAllContent();
        return  ResponseEntity.ok(news);
    }

    @PostMapping("/admin/news")
    public ResponseEntity<ContentDto> createNews (@RequestBody ContentDto contentDto){
        try {
            ContentDto created = contentService.createContent(contentDto);
            return ResponseEntity.ok(created);
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/admin/news/{id}")
    public ResponseEntity<ContentDto> updateNews(@PathVariable Long id, @RequestBody ContentDto contentDto){
        try{
            ContentDto updated = contentService.updateContent(id, contentDto);
            return ResponseEntity.ok(updated);
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/admin/news/{id}")
    public ResponseEntity<?> deleteNews(@PathVariable Long id){
        try{
            contentService.deleteContent(id);
            return ResponseEntity.ok(new MessageResponse("News deleted successfully"));
        }catch (Exception e){
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Failed to delete news: " + e.getMessage()));
        }
    }
}
