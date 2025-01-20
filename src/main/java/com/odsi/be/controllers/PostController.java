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
    public ResponseEntity<?> createPost(@AuthenticationPrincipal User user, @RequestBody PostDto dto) {
        try {
            return ResponseEntity.ok().body(postService.save(user, dto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping("/all")
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

    @GetMapping("/user/{username}")
    public ResponseEntity<?> getPostsByUserId(@PathVariable String username) {
        try {
            return ResponseEntity.ok(postService.getAllPosts(username));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping("/me")
    private List<PostDto> getMyPosts(@AuthenticationPrincipal User user) {
        return postService.getAllPosts(user);
    }

    @GetMapping("/{id}/signature")
    public ResponseEntity<?> isSignatureCorrect(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(postService.isSignatureCorrect(id));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
    
