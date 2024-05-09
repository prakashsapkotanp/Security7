package com.spring3.oauth.jwt.dtos;

import com.spring3.oauth.jwt.models.UserRole;
import lombok.*;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserLoginRequest {

    private Long id;
    private String username;
    private String password;
    private UserRole role;

}
