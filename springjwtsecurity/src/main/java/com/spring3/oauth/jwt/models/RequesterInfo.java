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
@Table(name = "REQUESTER")
public class RequesterInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;
    private String bloodGroup;
    private int pints;
    private double latitude;
    private double longitude;

    private double phone;
    private String name;
  //  @OneToOne(cascade = CascadeType.ALL)
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private UserInfo userInfo;
//    @OneToOne(cascade = CascadeType.ALL)
    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "locationID", referencedColumnName = "id")
    private MemberLocation memberLocation;
}
