package com.spring3.oauth.jwt.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_request")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "requester_id")
    private RequesterInfo requester;

//    private double currentRadius;
    private double currentLatitude;
    private double currentLongitude;
    private LocalDateTime createdAt;
    private boolean disabled = false;
    private int totalPintsDonated = 0;

    @ManyToOne
    @JoinColumn(name = "donor_id")
    private DonorInfo donorInfo;



}
