package com.odsi.be.model.post;

import com.odsi.be.model.user.User;
import com.odsi.be.security.validation.HtmlParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostConverter {
    private final HtmlParser htmlParser;

    public PostDto toDto(Post post) {
        String username = post.getUser() != null ? post.getUser().getName() : null;
        return new PostDto(post.getId(), post.getTitle(), post.getContent(), username);
    }

    public Post toEntity(PostDto postDto) {
        return Post.builder()
                .id(postDto.id())
                .title(postDto.title())
                .content(htmlParser.sanitizeContent(postDto.content()))
                .build();
    }

    public Post toEntity(PostDto postDto, User user) {
        return Post.builder()
                .id(postDto.id())
                .title(postDto.title())
                .content(htmlParser.sanitizeContent(postDto.content()))
                .user(user)
                .build();
    }

}
