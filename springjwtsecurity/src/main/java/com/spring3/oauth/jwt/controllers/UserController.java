package com.spring3.oauth.jwt.controllers;

import com.spring3.oauth.jwt.dtos.*;
import com.spring3.oauth.jwt.models.RefreshToken;
import com.spring3.oauth.jwt.services.JwtService;
import com.spring3.oauth.jwt.services.RefreshTokenService;
import com.spring3.oauth.jwt.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping(value = "/save")
    public ResponseEntity saveUser(@RequestBody UserLoginRequest userLoginRequest) {
        try {
            UserLoginResponse userLoginResponse = userService.saveUser(userLoginRequest);
            return ResponseEntity.ok(userLoginResponse);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/users")
    public ResponseEntity getAllUsers() {
        try {
            List<UserLoginResponse> userLoginResponses = userService.getAllUser();
            return ResponseEntity.ok(userLoginResponses);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/profile")
    public ResponseEntity<UserLoginResponse> getUserProfile() {
        try {
            UserLoginResponse userLoginResponse = userService.getUser();
            return ResponseEntity.ok().body(userLoginResponse);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/test")
    public String test() {
        try {
            return "Welcome";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> AuthenticateAndGetToken(@RequestBody AuthRequestDTO authRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(), authRequestDTO.getPassword()));
        if (authentication.isAuthenticated()) {
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequestDTO.getUsername());
            String accessToken = jwtService.GenerateToken(authRequestDTO.getUsername());
            JwtResponseDTO jwtResponseDTO = JwtResponseDTO.builder()
                    .accessToken(accessToken)
                    .token(refreshToken.getToken())
                    .build();
            return ResponseEntity.ok(jwtResponseDTO);
        } else {
            throw new UsernameNotFoundException("Invalid user request.");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<JwtResponseDTO> signUp(@RequestBody SignUpRequestDTO signUpRequestDTO) {
        // Check if the username already exists
        if (userService.existsByUsername(signUpRequestDTO.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        // Create a new user with default role "user"
        UserLoginRequest userLoginRequest = UserLoginRequest.builder()
                .username(signUpRequestDTO.getUsername())
                .password(signUpRequestDTO.getPassword())
                .build();

        // Save the new user
        UserLoginResponse userLoginResponse = userService.saveUser(userLoginRequest);

        // Generate JWT token
        String accessToken = jwtService.GenerateToken(signUpRequestDTO.getUsername());

        // Create a refresh token
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(signUpRequestDTO.getUsername());

        // Build and return JWT response
        JwtResponseDTO jwtResponseDTO = JwtResponseDTO.builder()
                .accessToken(accessToken)
                .token(refreshToken.getToken())
                .build();

        return ResponseEntity.ok(jwtResponseDTO);
    }


    @PostMapping("/refreshToken")
    public JwtResponseDTO refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {
        return refreshTokenService.findByToken(refreshTokenRequestDTO.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUserInfo)
                .map(userInfo -> {
                    String accessToken = jwtService.GenerateToken(userInfo.getUsername());
                    return JwtResponseDTO.builder()
                            .accessToken(accessToken)
                            .token(refreshTokenRequestDTO.getToken()).build();
                }).orElseThrow(() -> new RuntimeException("Refresh Token is not in DB..!!"));
    }
}
