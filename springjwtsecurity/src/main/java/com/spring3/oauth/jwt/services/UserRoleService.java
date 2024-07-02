package com.spring3.oauth.jwt.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.spring3.oauth.jwt.models.UserRole;
import com.spring3.oauth.jwt.repositories.UserRoleRepository;

@Service
public class UserRoleService {
    private final UserRoleRepository userRoleRepository;

    @Autowired
    public UserRoleService(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    public UserRole findByRoleName(String roleName) {
        return userRoleRepository.findByRoleName(roleName);
    }

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

    public UserRole save(UserRole userRole) {
        return userRoleRepository.save(userRole);
    }
    public UserRole updateUserRole(Long id, UserRole updatedRole) {
        Optional<UserRole> optionalRole = userRoleRepository.findById(id);
        if (optionalRole.isPresent()) {
            UserRole existingRole = optionalRole.get();
            existingRole.setRoleName(updatedRole.getRoleName());
            return userRoleRepository.save(existingRole);
        } else {
            throw new RuntimeException("Role not found with id: " + id);
        }
    }

}
