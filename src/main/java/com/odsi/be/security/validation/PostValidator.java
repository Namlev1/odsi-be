package com.odsi.be.security.validation;

import com.odsi.be.model.post.PostDto;
import org.springframework.stereotype.Service;

@Service
public class PostValidator {
    public boolean isValid(PostDto dto) {
        return !dto.title().isEmpty()
                && dto.title().length() < 100
                && !dto.content().isEmpty()
                && dto.content().length() < 5000;
    }
}
