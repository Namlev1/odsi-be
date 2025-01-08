package com.odsi.be.services;

import com.odsi.be.model.post.Post;
import com.odsi.be.model.post.PostConverter;
import com.odsi.be.model.post.PostDto;
import com.odsi.be.model.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository repository;
    private final PostConverter converter;

    public PostDto save(PostDto dto) {
        Post post = converter.toEntity(dto);
        Post savedPost = repository.save(post);
        return converter.toDto(savedPost);
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
