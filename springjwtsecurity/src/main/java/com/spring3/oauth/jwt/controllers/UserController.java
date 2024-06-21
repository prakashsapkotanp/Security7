package com.spring3.oauth.jwt.controllers;

import com.spring3.oauth.jwt.dtos.*;
import com.spring3.oauth.jwt.models.RefreshToken;
import com.spring3.oauth.jwt.models.UserRole;
import com.spring3.oauth.jwt.services.JwtService;
import com.spring3.oauth.jwt.services.RefreshTokenService;
import com.spring3.oauth.jwt.services.UserRoleService;
import com.spring3.oauth.jwt.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
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
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRoleService userRoleService;

    @PostMapping(value = "/save")
    public ResponseEntity<?> saveUser(@RequestBody UserLoginRequest userLoginRequest) {
        try {
            UserLoginResponse userLoginResponse = userService.saveUser(userLoginRequest);
            return ResponseEntity.ok(userLoginResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<UserLoginResponse> userLoginResponses = userService.getAllUser();
            return ResponseEntity.ok(userLoginResponses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }

    @PostMapping("/profile")
    public ResponseEntity<?> getUserProfile() {
        try {
            UserLoginResponse userLoginResponse = userService.getUser();
            return ResponseEntity.ok().body(userLoginResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/test")
    public String test() {
        return "Welcome";
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateAndGetToken(@RequestBody AuthRequestDTO authRequestDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(), authRequestDTO.getPassword()));

            if (authentication.isAuthenticated()) {
                RefreshToken refreshToken = refreshTokenService.generateUniqueRefreshToken(authRequestDTO.getUsername());
                String accessToken = jwtService.generateToken(authRequestDTO.getUsername());

                JwtResponseDTO jwtResponseDTO = JwtResponseDTO.builder()
                        .accessToken(accessToken)
                        .token(refreshToken.getToken())
                        .build();

                return ResponseEntity.ok(jwtResponseDTO);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ErrorResponseDTO(HttpStatus.UNAUTHORIZED.value(), "Invalid username or password"));
            }
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponseDTO(HttpStatus.UNAUTHORIZED.value(), "Invalid username or password"));
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to generate unique refresh token"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequestDTO signUpRequestDTO) {
        try {
            if (userService.existsByUsername(signUpRequestDTO.getUsername())) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponseDTO(HttpStatus.BAD_REQUEST.value(), "Username already exists"));
            }

            boolean isFirstRegistration = userService.countUsers() == 0;
            String roleName = isFirstRegistration ? "admin" : "user";
            UserRole userRole = userRoleService.findByRoleName(roleName);

            if (userRole == null) {
                userRole = new UserRole(roleName);
                userRole = userRoleService.saveUserRole(userRole);
            }

            UserLoginResponse userLoginResponse = userService.registerUser(signUpRequestDTO, userRole);
            String accessToken = jwtService.generateToken(signUpRequestDTO.getUsername());
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(signUpRequestDTO.getUsername());

            JwtResponseDTO jwtResponseDTO = JwtResponseDTO.builder()
                    .accessToken(accessToken)
                    .token(refreshToken.getToken())
                    .build();

            return ResponseEntity.ok(jwtResponseDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }

//    @PostMapping("/refreshToken")
//    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {
//        try {
//            return refreshTokenService.findByToken(refreshTokenRequestDTO.getToken())
//                    .map(refreshTokenService::verifyExpiration)
//                    .map(RefreshToken::getUserInfo)
//                    .map(userInfo -> {
//                        String accessToken = jwtService.generateToken(userInfo.getUsername());
//                        JwtResponseDTO jwtResponseDTO = JwtResponseDTO.builder()
//                                .accessToken(accessToken)
//                                .token(refreshTokenRequestDTO.getToken())
//                                .build();
//                        return ResponseEntity.ok(jwtResponseDTO);
//                    })
//                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
//                            .body(new ErrorResponseDTO(HttpStatus.NOT_FOUND.value(), "Refresh Token is not in DB")));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ErrorResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
//        }
//    }
@PostMapping("/refreshToken")
public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {
    try {
        return (ResponseEntity<?>) refreshTokenService.findByToken(refreshTokenRequestDTO.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUserInfo)
                .map(userInfo -> {
                    String accessToken = jwtService.generateToken(userInfo.getUsername());
                    JwtResponseDTO jwtResponseDTO = JwtResponseDTO.builder()
                            .accessToken(accessToken)
                            .token(refreshTokenRequestDTO.getToken()).build();
                    return (Object) ResponseEntity.ok().body(jwtResponseDTO);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDTO(HttpStatus.NOT_FOUND.value(), "Refresh Token is not in DB")));
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
    }
}

}
