package com.spring3.oauth.jwt.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.spring3.oauth.jwt.models.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    UserRole findByRoleName(String roleName);
    <S extends UserRole> S save(S entity);

}
