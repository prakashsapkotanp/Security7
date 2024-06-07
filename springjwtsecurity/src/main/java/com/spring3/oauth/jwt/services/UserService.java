package com.spring3.oauth.jwt.services;

import com.spring3.oauth.jwt.dtos.SignUpRequestDTO;
import com.spring3.oauth.jwt.dtos.UserLoginRequest;
import com.spring3.oauth.jwt.dtos.UserLoginResponse;
import com.spring3.oauth.jwt.models.UserRole;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserService {

    UserLoginResponse saveUser(UserLoginRequest userLoginRequest);

    UserLoginResponse getUser();

    List<UserLoginResponse> getAllUser();

    boolean existsByUsername(String username);

    UserLoginResponse registerUser(SignUpRequestDTO signUpRequestDTO);

    UserLoginResponse registerUser(SignUpRequestDTO signUpRequestDTO, UserRole userRole);

    UserDetails loadUserByUsername(String username);
}
