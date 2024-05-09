package com.spring3.oauth.jwt.services;

import com.spring3.oauth.jwt.models.RefreshToken;
import com.spring3.oauth.jwt.repositories.RefreshTokenRepository;
import com.spring3.oauth.jwt.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    UserRepository userRepository;

    public RefreshToken createRefreshToken(String username){
        RefreshToken refreshToken = RefreshToken.builder()
                .userInfo(userRepository.findByUsername(username))
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(600000))
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken generateUniqueRefreshToken(String username) {
        Optional<RefreshToken> existingToken = refreshTokenRepository.findByUserInfoUsername(username);
        if (existingToken.isPresent()) {
            // If a token already exists for the user, update its expiration date and return it
            RefreshToken token = existingToken.get();
            token.setExpiryDate(Instant.now().plusMillis(600000));
            return refreshTokenRepository.save(token);
        } else {
            // If no token exists, create a new one
            return createRefreshToken(username);
        }
    }

    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token){
        if(token.getExpiryDate().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + " Refresh token is expired. Please make a new login..!");
        }
        return token;
    }
}
