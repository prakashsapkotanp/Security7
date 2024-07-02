package com.spring3.oauth.jwt.repositories;

import com.spring3.oauth.jwt.helpers.RefreshableCRUDRepository;
import com.spring3.oauth.jwt.models.UserInfo;
import com.spring3.oauth.jwt.models.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;


@Repository
public interface UserRepository extends JpaRepository<UserInfo, Long> {

    UserInfo findByUsername(String username);
    UserInfo findFirstById(Long id);
    boolean existsByUsername(String username);
    void refresh(UserInfo userInfo);
    @Query("SELECT ur FROM UserInfo u JOIN u.roles ur WHERE u.username = :username")
    Set<UserRole> findRolesByUsername(@Param("username") String username);


}
