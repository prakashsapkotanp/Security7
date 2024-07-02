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
@Table(name = "DONOR")
public class DonorInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;
    private boolean status;

    //@OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "memberID", referencedColumnName = "id")
    private MemberInfo memberInfo;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "requesterID", referencedColumnName = "id")
    private RequesterInfo requesterInfo;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "locationID", referencedColumnName = "id")
    private MemberLocation memberLocation;
}
