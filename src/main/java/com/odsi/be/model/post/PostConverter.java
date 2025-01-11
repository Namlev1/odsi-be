package com.odsi.be.model.post;

import com.odsi.be.model.user.User;
import org.springframework.stereotype.Component;

@Component
public class PostConverter {
    public PostDto toDto(Post post) {
        Long userId = post.getUser() != null ? post.getUser().getId() : null;
        return new PostDto(post.getId(), post.getTitle(), post.getContent(), userId);
    }

    public Post toEntity(PostDto postDto) {
        return Post.builder()
                .id(postDto.id())
                .title(postDto.title())
                .content(postDto.content())
                .build();
    }

    public Post toEntity(PostDto postDto, User user) {
        return Post.builder()
                .id(postDto.id())
                .title(postDto.title())
                .content(postDto.content())
                .user(user)
                .build();
    }
}
