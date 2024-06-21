package com.spring3.oauth.jwt.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring3.oauth.jwt.models.UserRole;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole,Long> {
    UserRole findByRoleName(String roleName);

//    UserRole findByRoleName(String roleName);
//    List<UserRole> findByUserId(Long userId);
}
