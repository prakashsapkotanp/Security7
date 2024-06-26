package com.spring3.oauth.jwt.dtos;

import com.spring3.oauth.jwt.models.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserLoginResponse {

    private Long id;
    private String username;
    private List<UserRole> roles;


}
