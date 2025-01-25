package com.odsi.be.services;

import com.odsi.be.exceptions.InvalidPostException;
import com.odsi.be.model.post.Post;
import com.odsi.be.model.post.PostConverter;
import com.odsi.be.model.post.PostDto;
import com.odsi.be.model.post.PostRepository;
import com.odsi.be.model.user.User;
import com.odsi.be.model.user.UserRepository;
import com.odsi.be.security.validation.PostValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.hibernate.StaleObjectStateException;
import org.springframework.stereotype.Service;

import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log
public class PostService {
    private final PostRepository repository;
    private final PostConverter converter;
    private final PostValidator validator;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    private static void verifySignature(PublicKey publicKey, String content, byte[] signatureBytes) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initVerify(publicKey);
        sig.update(content.getBytes());

        if (!sig.verify(signatureBytes)) {
            throw new InvalidPostException("Signature verification failed");
        }
    }

    public List<PostDto> getAllPosts() {
        return repository.findAll().stream()
                .map(converter::toDto)
                .collect(Collectors.toList());
    }

    public List<PostDto> getAllPosts(User user) {
        return repository.findAllByUser(user).stream()
                .map(converter::toDto)
                .collect(Collectors.toList());
    }

    public List<PostDto> getAllPosts(String username) {
        User user = userRepository.findByName(username).orElseThrow();
        return getAllPosts(user);
    }


    public PostDto get(Long id) {
        Post post = repository.findById(id).orElseThrow();
        return converter.toDto(post);
    }

    @Transactional
    public PostDto save(User user, PostDto dto) {
        if (!validator.isValid(dto)) {
            throw new IllegalArgumentException("Post is not valid");
        }
        Post post = converter.toEntity(dto, user);

        if (post.getSignature() != null && !post.getSignature().isEmpty()) {
            throwIfInvalidSignature(user, post);
        }

        try {
            Post savedPost = repository.save(post);
            log.info("New post with ID: " + post.getId());
            return converter.toDto(savedPost);
        } catch (StaleObjectStateException e) {
            throw new RuntimeException("Invalid post id");
        }
    }

    private String normalizeContent(String content) {
        return content
                .replaceAll("\\s+", " ") // Replace multiple spaces with a single space
                .replaceAll(">\\s+<", "><") // Remove spaces between tags
                .trim(); // Trim leading and trailing spaces
    }
    
    private void throwIfInvalidSignature(User user, Post post) {
        String pubKey = user.getPublicKey();
        if (pubKey == null) {
            throw new InvalidPostException("User does not have a public key");
        }
        String signature = post.getSignature();
        String content = post.getContent();

        String normalizedContent = normalizeContent(content); // Normalize content
        System.out.println("Normalized content being verified: " + normalizedContent);

        try {
            // Convert the PEM-formatted public key to a usable PublicKey object
            String base64Key = pubKey
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replace("\n", "")
                    .trim();

            byte[] keyBytes = Base64.getDecoder().decode(base64Key);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(keySpec);

            // Decode the signature from Base64
            byte[] signatureBytes = Base64.getDecoder().decode(signature);

            verifySignature(publicKey, normalizedContent, signatureBytes);
        } catch (Exception e) {
            throw new InvalidPostException("An error occurred while verifying the signature: " + e.getMessage());
        }
    }
    public boolean isSignatureCorrect(Long id) {
        Post post = postRepository.findById(id).orElseThrow();
        User user = post.getUser();
        try {
            throwIfInvalidSignature(user, post);
        } catch (InvalidPostException e) {
            if (e.getMessage().equals("An error occurred while verifying the signature: Signature verification failed")) {
                return false;
            }
            throw e;
        }
        return true;
    }
}
