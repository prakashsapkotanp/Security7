package com.spring3.oauth.jwt.repositories;

import com.spring3.oauth.jwt.models.RefreshToken;
import com.spring3.oauth.jwt.models.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {

    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUserInfoUsername(String username);
}
