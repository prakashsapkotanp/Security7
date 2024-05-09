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
    UserRepository userRepository;

    ModelMapper modelMapper = new ModelMapper();

    @Override
    public UserLoginResponse saveUser(UserLoginRequest userLoginRequest) {
        if(userLoginRequest.getUsername() == null){
            throw new RuntimeException("Parameter username is not found in request..!!");
        } else if(userLoginRequest.getPassword() == null){
            throw new RuntimeException("Parameter password is not found in request..!!");
        }

        UserInfo savedUser = null;

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = userLoginRequest.getPassword();
        String encodedPassword = encoder.encode(rawPassword);

        UserInfo user = modelMapper.map(userLoginRequest, UserInfo.class);
        user.setPassword(encodedPassword);
        if(userLoginRequest.getId() != null){
            UserInfo oldUser = userRepository.findFirstById(userLoginRequest.getId());
            if(oldUser != null){
                oldUser.setId(user.getId());
                oldUser.setPassword(user.getPassword());
                oldUser.setUsername(user.getUsername());
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
        UserLoginResponse userLoginResponse = modelMapper.map(savedUser, UserLoginResponse.class);
        return userLoginResponse;
    }

    @Override
    public UserLoginResponse getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetail = (UserDetails) authentication.getPrincipal();
        String usernameFromAccessToken = userDetail.getUsername();
        UserInfo user = userRepository.findByUsername(usernameFromAccessToken);
        UserLoginResponse userLoginResponse = modelMapper.map(user, UserLoginResponse.class);
        return userLoginResponse;
    }

    @Override
    public List<UserLoginResponse> getAllUser() {
        List<UserInfo> users = (List<UserInfo>) userRepository.findAll();
        Type setOfDTOsType = new TypeToken<List<UserLoginResponse>>(){}.getType();
        List<UserLoginResponse> userLoginRespons = modelMapper.map(users, setOfDTOsType);
        return userLoginRespons;
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

        // Create a Set containing the default role "user"
        Set<UserRole> roles = new HashSet<>();
        roles.add(new UserRole("user")); // Assuming UserRole constructor exists

        UserInfo newUser = UserInfo.builder()
                .username(signUpRequestDTO.getUsername())
                .password(encodedPassword)
                .roles(roles) // Set roles
                .build();

        UserInfo savedUser = userRepository.save(newUser);

        UserLoginResponse userLoginResponse = modelMapper.map(savedUser, UserLoginResponse.class);

        return userLoginResponse;
    }
    @Override
    public UserLoginResponse registerUser(SignUpRequestDTO signUpRequestDTO, UserRole userRole) {
        // Create a Set containing the user's role
        Set<UserRole> roles = new HashSet<>();
        roles.add(userRole);

        // Create the user with the associated role
        UserLoginRequest userLoginRequest = UserLoginRequest.builder()
                .username(signUpRequestDTO.getUsername())
                .password(signUpRequestDTO.getPassword())
                .role(userRole)
                .build();

        // Save the user
        return saveUser(userLoginRequest);
    }

}
