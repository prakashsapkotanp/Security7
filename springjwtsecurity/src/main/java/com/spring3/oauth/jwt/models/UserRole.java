package com.spring3.oauth.jwt.models;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USER_ROLES")
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ROLE_ID")
    private long id;

    @Column(name = "ROLE_NAME")
    private String roleName;

    // Constructor with roleName parameter

    public UserRole(String roleName) {
        this.roleName = roleName;
    }

    public String getRole( long) {

    }

}
