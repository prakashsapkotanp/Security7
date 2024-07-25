package com.spring3.oauth.jwt.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "requester_id")
    @JsonManagedReference
    private RequesterInfo requester;

    private double currentLatitude;
    private double currentLongitude;
    private LocalDateTime createdAt;
    private boolean disabled = false;
    private int totalPintsDonated = 0;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "donor_id", nullable = true)
    private DonorInfo donorInfo;

    public Request(RequesterInfo requester,double currentLatitude, double currentLongitude, LocalDateTime now, int totalPintsDonated) {
        this.requester = requester;
        this.currentLatitude = currentLatitude;
        this.currentLongitude = currentLongitude;
        this.createdAt = now;
        this.totalPintsDonated = totalPintsDonated;
    }
}