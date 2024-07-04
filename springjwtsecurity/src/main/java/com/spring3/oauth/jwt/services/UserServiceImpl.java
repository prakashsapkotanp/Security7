package com.spring3.oauth.jwt.services;

import com.spring3.oauth.jwt.dtos.*;
import com.spring3.oauth.jwt.models.UserInfo;
import com.spring3.oauth.jwt.models.UserRole;
import com.spring3.oauth.jwt.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleService userRoleService;

    private ModelMapper modelMapper = new ModelMapper();


    @Override
    public UserLoginResponse saveUser(UserLoginRequest userLoginRequest) {
        if (userLoginRequest.getUsername() == null) {
            throw new RuntimeException("Parameter 'username' is not found in request.");
        } else if (userLoginRequest.getPassword() == null) {
            throw new RuntimeException("Parameter 'password' is not found in request.");
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(userLoginRequest.getPassword());

        UserInfo user = modelMapper.map(userLoginRequest, UserInfo.class);
        user.setPassword(encodedPassword);
        user.setRoles(new HashSet<>());

        if (userLoginRequest.getRole() != null) {
            UserRole userRole = userRoleService.findByRoleName("ROLE_" + userLoginRequest.getRole().getRoleName().toUpperCase());
            if (userRole == null) {
                userRole = userRoleService.save(new UserRole("ROLE_" + userLoginRequest.getRole().getRoleName().toUpperCase()));
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
    public UserLoginResponse getUserById(Long id) {
        UserInfo user = userRepository.findFirstById(id);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + id);
        }
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
    public List<UserInfo> getAll() {
        return userRepository.findAll();
//        Type setOfDTOsType = new TypeToken<List<UserLoginResponse>>() {
//        }.getType();
//        return modelMapper.map(users, setOfDTOsType);
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

        // Check if this is the first user
        if (userRepository.count() == 0) {
            // Make the first user an admin
            UserRole adminRole = userRoleService.findByRoleName("ROLE_ADMIN");
            if (adminRole == null) {
                adminRole = userRoleService.save(new UserRole("ROLE_ADMIN"));
            }
            roles.add(adminRole);
        } else {
            // Make others as users
            UserRole defaultRole = userRoleService.findByRoleName("ROLE_USER");
            if (defaultRole == null) {
                defaultRole = userRoleService.save(new UserRole("ROLE_USER"));
            }
            roles.add(defaultRole);
        }

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
        UserRole role = userRoleService.findByRoleName("ROLE_" + userRole.getRoleName().toUpperCase());
        if (role == null) {
            role = userRoleService.save(new UserRole("ROLE_" + userRole.getRoleName().toUpperCase()));
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
    /**
     @Override
     public UserDetails loadUserByUsername(String username) {
     UserInfo user = userRepository.findByUsername(username);
     //        for(UserRole r:user.getRoles()){
     //
     //            System.out.println(r.getRoleName());
     //
     //        }
     //        System.out.println("test point");
     //        if (user == null) {
     //            throw new RuntimeException("User not found");
     //        }


     return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
     .password(user.getPassword())
     .authorities(user.getRoles().stream()
     .map(role -> role.getRoleName().toUpperCase())
     .toArray(String[]::new))
     .build();
     }
     **/
//@Override
//public UserDetails loadUserByUsername(String username) {
//    UserInfo user = userRepository.findByUsername(username);
//    if (user == null) {
//        throw new RuntimeException("User not found");
//    }
//
//    Set<GrantedAuthority> authorities = getUserRoleByUsername(username).stream()
//            .map(role -> new SimpleGrantedAuthority(role.toUpperCase()))
//            .collect(Collectors.toSet());
//
//        for(UserRole r:user.getRoles()){
//
//            System.out.println(r.getRoleName());
//
//        }
//        System.out.println("test point");
//        if (user == null) {
//            throw new RuntimeException("User not found");
//        }

//    return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
//            .password(user.getPassword())
//            .authorities(authorities)
//            .build();
//}
    @Override
    public UserDetails loadUserByUsername(String username) {
        UserInfo user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        Set<GrantedAuthority> authorities = getUserRoleByUsername(username).stream()
                .map(role -> new SimpleGrantedAuthority(role.toUpperCase()))
                .collect(Collectors.toSet());

        return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }

    //    @Override
//    public Set<String> getUserRoleByUsername(String username) {
//        UserInfo user = userRepository.findByUsername(username);
//        if (user != null && user.getRoles() != null) {
//
//            System.out.println("----------getUserRoleByUsername-------------");
//
//            System.out.println(user.getRoles().stream()
//                    .map(UserRole::getRoleName)
//                    .collect(Collectors.toSet()));
//
//            return user.getRoles().stream()
//                    .map(UserRole::getRoleName)
//                    .collect(Collectors.toSet());
//        }
//        return new HashSet<>();
//    }
    @Override
    public Set<String> getUserRoleByUsername(String username) {
        Set<UserRole> roles = userRepository.findRolesByUsername(username);
        if (roles != null) {
            return roles.stream()
                    .map(UserRole::getRoleName)
                    .collect(Collectors.toSet());
        }
        return new HashSet<>();
    }



}