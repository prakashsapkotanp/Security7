package com.spring3.oauth.jwt.dtos;

import com.spring3.oauth.jwt.models.DonorInfo;
import com.spring3.oauth.jwt.models.RequesterInfo;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RequestDTO {
    private Long id;
    private RequesterInfo requester;
    private double currentLatitude;
    private double currentLongitude;
    private LocalDateTime createdAt;
    private boolean disabled;
    private int totalPintsDonated;
    private DonorInfo donorInfo;


}
