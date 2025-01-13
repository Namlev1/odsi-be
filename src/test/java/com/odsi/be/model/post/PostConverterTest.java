package com.odsi.be.model.post;

import com.odsi.be.model.user.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PostConverterTest {

    @Test
    void convertPostWithEmptyTitleToDto() {
        // Arrange
        Post post = new Post(1L, "", "Content", null);
        PostConverter converter = new PostConverter();

        // Act
        PostDto dto = converter.toDto(post);

        // Assert
        assertNotNull(dto);
        assertEquals(1, dto.id());
        assertEquals("", dto.title());
        assertEquals("Content", dto.content());
    }

    @Test
    void convertPostWithEmptyContentToDto() {
        // Arrange
        Post post = new Post(1L, "Title", "", null);
        PostConverter converter = new PostConverter();

        // Act
        PostDto dto = converter.toDto(post);

        // Assert
        assertNotNull(dto);
        assertEquals(1, dto.id());
        assertEquals("Title", dto.title());
        assertEquals("", dto.content());
    }

    @Test
    void convertDtoWithEmptyTitleToPost() {
        // Arrange
        PostDto dto = new PostDto(1L, "", "Content", null);
        PostConverter converter = new PostConverter();

        // Act
        Post post = converter.toEntity(dto);

        // Assert
        assertNotNull(post);
        assertEquals(1, post.getId());
        assertEquals("", post.getTitle());
        assertEquals("Content", post.getContent());
    }

    @Test
    void convertDtoWithEmptyContentToPost() {
        // Arrange
        PostDto dto = new PostDto(1L, "Title", "", null);
        PostConverter converter = new PostConverter();

        // Act
        Post post = converter.toEntity(dto);

        // Assert
        assertNotNull(post);
        assertEquals(1, post.getId());
        assertEquals("Title", post.getTitle());
        assertEquals("", post.getContent());
    }

    @Test
    void convertPostWithUser() {
        // Arrange
        User user = new User(2L, "username", "password", null);
        Post post = new Post(1L, "Title", "Content", user);
        PostConverter converter = new PostConverter();

        // Act
        PostDto dto = converter.toDto(post);

        // Assert
        assertNotNull(dto);
        assertEquals(1, dto.id());
        assertEquals("Title", dto.title());
        assertEquals("Content", dto.content());
        assertEquals("username", dto.username());
    }
}