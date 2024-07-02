package com.spring3.oauth.jwt.dtos;

import lombok.Data;

import java.util.List;

@Data
public class UserUpdateRequestDTO {
    private String username;
    private String password;

}
