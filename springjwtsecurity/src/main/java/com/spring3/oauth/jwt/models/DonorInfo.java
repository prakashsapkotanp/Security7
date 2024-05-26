package com.spring3.oauth.jwt.models;

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
@Table(name = "DONOR")
public class DonorInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;
    private boolean status;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "memberID", referencedColumnName = "id")
    private MemberInfo memberInfo;
     @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "requesterID", referencedColumnName = "id")
    private RequesterInfo requesterInfo;
}
