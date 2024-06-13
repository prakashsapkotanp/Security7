package com.spring3.oauth.jwt.controllers;

import com.spring3.oauth.jwt.dtos.*;
import com.spring3.oauth.jwt.models.RefreshToken;
import com.spring3.oauth.jwt.models.UserInfo;
import com.spring3.oauth.jwt.models.UserRole;
import com.spring3.oauth.jwt.repositories.UserRepository;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

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


    //test
    @Autowired
    UserRepository userRepository;

    @PostMapping(value = "/save")
    public ResponseEntity<?> saveUser(@RequestBody UserLoginRequest userLoginRequest) {
        try {
            UserLoginResponse userLoginResponse = userService.saveUser(userLoginRequest);
            return ResponseEntity.ok(userLoginResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }

//    @GetMapping("/users")
//    public ResponseEntity<?> getAllUsers() {
//        try {
//            List<UserLoginResponse> userLoginResponses = userService.getAllUser();
//            return ResponseEntity.ok(userLoginResponses);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
//        }
//    }
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        try {
            for(UserInfo info: userRepository.findAll()){
                System.out.println(info.getUsername());
                for(UserRole r:info.getRoles()){
                    System.out.println(r.getRoleName());
                }
            }
            List<UserInfo> resp = userService.getAll();
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getUser() {
        try {
//            Set<UserRole> rs=userRepository.findRolesByUsername("suman");
//              //  System.out.println(info.getUsername());
//                for(UserRole r:rs){
//                    System.out.println(r.getRoleName());
//                }
//            Set<UserRole> rs1=userRepository.findRolesByUsername("demo");
//            //  System.out.println(info.getUsername());
//            for(UserRole r:rs1){
//                System.out.println(r.getRoleName());
//            }

            List<UserInfo> resp = userService.getAll();

            String user1 = resp.get(0).getUsername();

            String user2 = resp.get(resp.size()-1).getUsername();

            Set<UserRole> rs=userRepository.findRolesByUsername(user1);
             //  System.out.println(info.getUsername());
                for(UserRole r:rs){
                    resp.get(0).getRoles().add(r);
                   System.out.println(r.getRoleName());
              }

            Set<UserRole> rs2=userRepository.findRolesByUsername(user2);
            //  System.out.println(info.getUsername());
            for(UserRole r:rs2){
                resp.get(resp.size()-1).getRoles().add(r);
                System.out.println(r.getRoleName());
            }

            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }


    @PostMapping("/profile")
    public ResponseEntity<?> getUserProfile() {
        try {
            UserLoginResponse userLoginResponse = userService.getUser();
            return ResponseEntity.ok().body(userLoginResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
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
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(), authRequestDTO.getPassword()));

            // If authentication is successful
            if (authentication.isAuthenticated()) {
                // Generate unique refresh token
                RefreshToken refreshToken = refreshTokenService.generateUniqueRefreshToken(authRequestDTO.getUsername());

                // Generate JWT token with roles and username
//                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//                String accessToken = jwtService.generateToken(userDetails);
                UserDetails userDetails = userService.loadUserByUsername(authRequestDTO.getUsername());
                for(GrantedAuthority g:userDetails.getAuthorities()){
                    System.out.println(g.getAuthority());

                }
                String accessToken = jwtService.generateToken(userDetails);

                // Build JWT response DTO
                JwtResponseDTO jwtResponseDTO = JwtResponseDTO.builder()
                        .accessToken(accessToken)
                        .token(refreshToken.getToken())
                        .build();

                // Return JWT response with OK status
                return ResponseEntity.ok(jwtResponseDTO);
            } else {
                // Return unauthorized status if authentication fails
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponseDTO(HttpStatus.UNAUTHORIZED.value(), "Invalid username or password"));
            }
        } catch (UsernameNotFoundException e) {
            // Return unauthorized status if username not found
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponseDTO(HttpStatus.UNAUTHORIZED.value(), "Invalid username or password"));
        } catch (DuplicateKeyException e) {
            // Handle duplicate token error gracefully
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to generate unique refresh token"));
        } catch (Exception e) {
            // Return internal server error status for other exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }



    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequestDTO signUpRequestDTO) {
        try {
            // Check if the username already exists
            if (userService.existsByUsername(signUpRequestDTO.getUsername())) {
                return ResponseEntity.badRequest().body(new ErrorResponseDTO(HttpStatus.BAD_REQUEST.value(), "Username already exists"));
            }

            // Register the user
            UserLoginResponse userLoginResponse = userService.registerUser(signUpRequestDTO);

            // Generate JWT token with roles
            UserDetails userDetails = userService.loadUserByUsername(signUpRequestDTO.getUsername());
            String accessToken = jwtService.generateToken(userDetails);

            // Create a refresh token
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(signUpRequestDTO.getUsername());

            // Build and return JWT response
            JwtResponseDTO jwtResponseDTO = JwtResponseDTO.builder()
                    .accessToken(accessToken)
                    .token(refreshToken.getToken())
                    .build();

            return ResponseEntity.ok(jwtResponseDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }
    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {
        try {
            return (ResponseEntity<?>) refreshTokenService.findByToken(refreshTokenRequestDTO.getToken())
                    .map(refreshTokenService::verifyExpiration)
                    .map(RefreshToken::getUserInfo)
                    .map(userInfo -> {
                        UserDetails userDetails = userService.loadUserByUsername(userInfo.getUsername());
                        String accessToken = jwtService.generateToken(userDetails);
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
