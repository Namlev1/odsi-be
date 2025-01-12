package com.odsi.be.controllers;

import com.odsi.be.model.post.PostDto;
import com.odsi.be.model.user.User;
import com.odsi.be.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<?> createPost(@AuthenticationPrincipal User user, PostDto dto) {
        try {
            return ResponseEntity.ok().body(postService.save(user, dto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping
    public List<PostDto> getAllPosts() {
        return postService.getAllPosts();
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable Long id) {
        try {
            PostDto postDto = postService.get(id);
            return ResponseEntity.ok(postDto);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

//    @GetMapping("/me")
//    private List<PostDto> getMyPosts(@AuthenticationPrincipal User user) {
//        return postService.getAllPosts(user);
//    }
    
    @GetMapping("/me")
    public List<PostDto> get() {
        return List.of(
                new PostDto(1L, "title1", "content", 1L),
                new PostDto(2L, "title2", "content", 2L)
        );
    }
}
    
