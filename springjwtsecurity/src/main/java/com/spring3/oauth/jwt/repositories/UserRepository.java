package com.spring3.oauth.jwt.repositories;

import com.spring3.oauth.jwt.helpers.RefreshableCRUDRepository;
import com.spring3.oauth.jwt.models.UserInfo;
import com.spring3.oauth.jwt.models.UserRole;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends RefreshableCRUDRepository<UserInfo, Long> {

//   public UserInfo findByUsername(String username);
//   UserInfo findFirstById(Long id);
//    boolean existsByUsername(String username);
    UserInfo findByUsername(String username);
    UserInfo findFirstById(Long id);
    boolean existsByUsername(String username);
    void refresh(UserInfo userInfo);

}
