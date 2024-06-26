package com.spring3.oauth.jwt.models;

import java.sql.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "MEMBERS")
public class MemberInfo {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "ID",unique=true)
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
    @Column(unique = true)
    private String email;
    private Date lastTimeOfDonation;
    private Date registrationDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private UserInfo userInfo;
   

}
