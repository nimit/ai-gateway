package com.nimit.aigateway.services;

import com.nimit.aigateway.model.User;
import com.nimit.aigateway.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class MagicLinkService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private UserRepository userRepository;

    private static final String MAGIC_LINK_PREFIX = "magic_link:";
    private static final Duration TOKEN_VALIDITY = Duration.ofMinutes(5);

    @Transactional
    public String generateToken(String email) {
        userRepository.findByEmail(email)
                .orElseGet(() -> createNewUser(email));

        String token = UUID.randomUUID().toString();

        redisTemplate.opsForValue().set(
                MAGIC_LINK_PREFIX + token,
                email,
                TOKEN_VALIDITY);

        return token;
    }

    private User createNewUser(String email) {
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setIsActive(true);
        newUser.setCreatedAt(LocalDateTime.now());
        return userRepository.save(newUser);
    }

    @Transactional
    public User validateToken(String token) {
        String email = redisTemplate.opsForValue().get(MAGIC_LINK_PREFIX + token);

        if (email == null) {
            throw new RuntimeException("User not found");
        }
        redisTemplate.delete(MAGIC_LINK_PREFIX + token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setLastLogin(LocalDateTime.now());
        return userRepository.save(user);

    }
}
