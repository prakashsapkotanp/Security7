package com.spring3.oauth.jwt.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "MEMBER_LOCATION")
public class MemberLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;
    private double latitude;
    private double longitude;
    
   
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "memberLocation", referencedColumnName = "id")
    private MemberInfo memberInfo;
}
