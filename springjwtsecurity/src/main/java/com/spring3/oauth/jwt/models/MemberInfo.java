package com.spring3.oauth.jwt.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;

@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "MEMBERS")
public class MemberInfo {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "ID", unique=true)
    private Long id;
    @Column(length = 20)
    private String firstname;
    @Column(length = 20)
    private String middlename;
    @Column(length = 20)
    private String lastname;
    private Date dateOfBirth;
    @Column(length = 6)
    private String bloodGroup;
    @Column(length = 6)
    private String gender;
    private Date lastTimeOfDonation;
    private Date registrationDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private UserInfo userInfo;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "locationId", referencedColumnName = "id")
    private MemberLocation memberLocation;
}
