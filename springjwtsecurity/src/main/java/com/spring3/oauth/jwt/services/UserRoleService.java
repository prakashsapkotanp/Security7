package com.spring3.oauth.jwt.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.spring3.oauth.jwt.models.UserRole;
import com.spring3.oauth.jwt.repositories.UserRoleRepository;

@Service
public class UserRoleService {
    private final UserRoleRepository userRoleRepository;

    public UserRoleService(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

//    public UserRole findByRoleName(String roleName) {
//        return userRoleRepository.findByRoleName(roleName);
//    }
//
//    public List<UserRole> findByUserId(Long userId){
//        return userRoleRepository.findByUserId(userId);
//    }

    public List<UserRole> getAllUserRoles() {
        return userRoleRepository.findAll();
    }

    public Optional<UserRole> getUserRoleById(Long id) {
        return userRoleRepository.findById(id);
    }

    public UserRole saveUserRole(UserRole userRole) {
        return userRoleRepository.save(userRole);
    }

    public void deleteUserRole(Long id) {
        userRoleRepository.deleteById(id);
    }

    public UserRole findByRoleName(String roleName) {
        return userRoleRepository.findByRoleName(roleName);
    }
//    public List<String> getRolesByUserId(Long userId) {
//        List<UserRole> roles = userRoleRepository.findByUserId(userId);
//        return roles.stream().map(UserRole::getRoleName).collect(Collectors.toList());
//    }

}
