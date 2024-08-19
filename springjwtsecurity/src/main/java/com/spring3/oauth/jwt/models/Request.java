package com.spring3.oauth.jwt.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_request")
public class Request {


    @Setter
    @Getter
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
    @JoinColumn(name = "donor_id")
    private DonorInfo donorInfo;

    public Request(RequesterInfo requester, double currentLatitude, double currentLongitude, LocalDateTime now, int totalPintsDonated) {
        this.requester = requester;
        this.currentLatitude = currentLatitude;
        this.currentLongitude = currentLongitude;
        this.createdAt = now;
        this.totalPintsDonated = totalPintsDonated;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getCurrentLatitude() {
        return currentLatitude;
    }

    public void setCurrentLatitude(double currentLatitude) {
        this.currentLatitude = currentLatitude;
    }

    public RequesterInfo getRequester() {
        return requester;
    }

    public void setRequester(RequesterInfo requester) {
        this.requester = requester;
    }

    public double getCurrentLongitude() {
        return currentLongitude;
    }

    public void setCurrentLongitude(double currentLongitude) {
        this.currentLongitude = currentLongitude;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public int getTotalPintsDonated() {
        return totalPintsDonated;
    }

    public void setTotalPintsDonated(int totalPintsDonated) {
        this.totalPintsDonated = totalPintsDonated;
    }

    public DonorInfo getDonorInfo() {
        return donorInfo;
    }

    public void setDonorInfo(DonorInfo donorInfo) {
        this.donorInfo = donorInfo;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}