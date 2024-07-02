package com.spring3.oauth.jwt.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "DONOR")
public class DonorInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    private boolean status;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private MemberInfo memberInfo;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "requester_id", referencedColumnName = "id")
    @JsonIgnore
    private RequesterInfo requesterInfo;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    @JsonIgnore
    private MemberLocation memberLocation;

    @OneToMany(mappedBy = "donorInfo", cascade = CascadeType.ALL)
    private List<Request> requests;
}
