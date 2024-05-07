package com.spring3.oauth.jwt.services;

import com.spring3.oauth.jwt.dtos.SignUpRequestDTO;
import com.spring3.oauth.jwt.dtos.UserLoginRequest;
import com.spring3.oauth.jwt.dtos.UserLoginResponse;

import java.util.List;


public interface UserService {

    UserLoginResponse saveUser(UserLoginRequest userLoginRequest);

    UserLoginResponse getUser();

    List<UserLoginResponse> getAllUser();


    boolean existsByUsername(String username);
    UserLoginResponse registerUser(SignUpRequestDTO signUpRequestDTO);
}