package com.spring3.oauth.jwt.services;

import com.spring3.oauth.jwt.dtos.SignUpRequestDTO;
import com.spring3.oauth.jwt.dtos.UserLoginRequest;
import com.spring3.oauth.jwt.dtos.UserLoginResponse;
import com.spring3.oauth.jwt.models.UserInfo;
import com.spring3.oauth.jwt.models.UserRole;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Set;

public interface UserService {

    UserLoginResponse saveUser(UserLoginRequest userLoginRequest);

    UserLoginResponse getUser();

    List<UserLoginResponse> getAllUser();

    boolean existsByUsername(String username);

    UserLoginResponse registerUser(SignUpRequestDTO signUpRequestDTO);

    UserLoginResponse registerUser(SignUpRequestDTO signUpRequestDTO, UserRole userRole);

    UserDetails loadUserByUsername(String username);
    public Set<String> getUserRoleByUsername(String username);

  //  List<UserInfo> findAllWithRoles(); // get all users with roles
  public List<UserInfo> getAll();
}
