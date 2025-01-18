package com.odsi.be.model.post;

import com.odsi.be.model.user.User;
import com.odsi.be.security.validation.HtmlParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostConverterTest {

    @Mock
    private HtmlParser htmlParser;

    @InjectMocks
    private PostConverter converter;

    @Test
    void convertPostWithEmptyTitleToDto() {
        // Arrange
        Post post = new Post(1L, "", "Content", null, null);

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
        Post post = new Post(1L, "Title", "", null, null);

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
        PostDto dto = new PostDto(1L, "", "Content", null, null);
        when(htmlParser.sanitizeContent("Content")).thenReturn("Content");

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
        PostDto dto = new PostDto(1L, "Title", "", null, null);
        when(htmlParser.sanitizeContent("")).thenReturn("");

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
        User user = new User(2L, "username", "password", null, null, null);
        Post post = new Post(1L, "Title", "Content", null, user);

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