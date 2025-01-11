package com.odsi.be.services;

import com.odsi.be.model.post.Post;
import com.odsi.be.model.post.PostConverter;
import com.odsi.be.model.post.PostDto;
import com.odsi.be.model.post.PostRepository;
import com.odsi.be.model.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.StaleObjectStateException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository repository;
    private final PostConverter converter;

    @Transactional
    public PostDto save(User user, PostDto dto) {
        Post post = converter.toEntity(dto, user);
        try {
            Post savedPost = repository.save(post);
            return converter.toDto(savedPost);
        } catch (StaleObjectStateException e) {
            throw new RuntimeException("Invalid post id");
        }
    }

    public List<PostDto> getAllPosts() {
        return repository.findAll().stream()
                .map(converter::toDto)
                .collect(Collectors.toList());
    }

    public PostDto get(Long id) {
        Post post = repository.findById(id).orElseThrow();
        return converter.toDto(post);
    }

}
