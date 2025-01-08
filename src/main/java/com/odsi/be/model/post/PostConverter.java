package com.odsi.be.model.post;

import org.springframework.stereotype.Service;

@Service
public class PostConverter {
    public PostDto toDto(Post post) {
        return new PostDto(post.getId(), post.getTitle(), post.getContent());
    }

    public Post toEntity(PostDto postDto) {
        return Post.builder()
                .id(postDto.id())
                .title(postDto.title())
                .content(postDto.content())
                .build();
    }
}
