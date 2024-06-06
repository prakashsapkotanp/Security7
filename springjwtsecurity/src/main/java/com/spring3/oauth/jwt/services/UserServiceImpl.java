package com.spring3.oauth.jwt.services;

import com.spring3.oauth.jwt.dtos.*;
import com.spring3.oauth.jwt.models.UserInfo;
import com.spring3.oauth.jwt.models.UserRole;
import com.spring3.oauth.jwt.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleService userRoleService; // Assuming you have a UserRoleService to manage roles

    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public UserLoginResponse saveUser(UserLoginRequest userLoginRequest) {
        if (userLoginRequest.getUsername() == null) {
            throw new RuntimeException("Parameter username is not found in request..!!");
        } else if (userLoginRequest.getPassword() == null) {
            throw new RuntimeException("Parameter password is not found in request..!!");
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(userLoginRequest.getPassword());

        UserInfo user = modelMapper.map(userLoginRequest, UserInfo.class);
        user.setPassword(encodedPassword);
        user.setRoles(new HashSet<>()); // Initialize roles

        if (userLoginRequest.getRole() != null) {
            UserRole userRole = userRoleService.findByRoleName(userLoginRequest.getRole().getRoleName());
            if (userRole == null) {
                userRole = userRoleService.save(new UserRole(userLoginRequest.getRole().getRoleName()));
            }
            user.getRoles().add(userRole);
        }

        UserInfo savedUser;
        if (userLoginRequest.getId() != null) {
            UserInfo oldUser = userRepository.findFirstById(userLoginRequest.getId());
            if (oldUser != null) {
                oldUser.setUsername(user.getUsername());
                oldUser.setPassword(user.getPassword());
                oldUser.setRoles(user.getRoles());

                savedUser = userRepository.save(oldUser);
                userRepository.refresh(savedUser);
            } else {
                throw new RuntimeException("Can't find record with identifier: " + userLoginRequest.getId());
            }
        } else {
            savedUser = userRepository.save(user);
        }

        userRepository.refresh(savedUser);
        return modelMapper.map(savedUser, UserLoginResponse.class);
    }

    @Override
    public UserLoginResponse getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetail = (UserDetails) authentication.getPrincipal();
        String usernameFromAccessToken = userDetail.getUsername();
        UserInfo user = userRepository.findByUsername(usernameFromAccessToken);
        return modelMapper.map(user, UserLoginResponse.class);
    }

    @Override
    public List<UserLoginResponse> getAllUser() {
        List<UserInfo> users = (List<UserInfo>) userRepository.findAll();
        Type setOfDTOsType = new TypeToken<List<UserLoginResponse>>() {
        }.getType();
        return modelMapper.map(users, setOfDTOsType);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public UserLoginResponse registerUser(SignUpRequestDTO signUpRequestDTO) {
        if (existsByUsername(signUpRequestDTO.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(signUpRequestDTO.getPassword());

        Set<UserRole> roles = new HashSet<>();
        UserRole defaultRole = userRoleService.findByRoleName("user");
        if (defaultRole == null) {
            defaultRole = userRoleService.save(new UserRole("user"));
        }
        roles.add(defaultRole);

        UserInfo newUser = UserInfo.builder()
                .username(signUpRequestDTO.getUsername())
                .password(encodedPassword)
                .roles(roles)
                .build();

        UserInfo savedUser = userRepository.save(newUser);
        return modelMapper.map(savedUser, UserLoginResponse.class);
    }

    @Override
    public UserLoginResponse registerUser(SignUpRequestDTO signUpRequestDTO, UserRole userRole) {
        Set<UserRole> roles = new HashSet<>();
        UserRole role = userRoleService.findByRoleName(userRole.getRoleName());
        if (role == null) {
            role = userRoleService.save(userRole);
        }
        roles.add(role);

        UserInfo newUser = UserInfo.builder()
                .username(signUpRequestDTO.getUsername())
                .password(new BCryptPasswordEncoder().encode(signUpRequestDTO.getPassword()))
                .roles(roles)
                .build();

        UserInfo savedUser = userRepository.save(newUser);
        return modelMapper.map(savedUser, UserLoginResponse.class);
    }
}
