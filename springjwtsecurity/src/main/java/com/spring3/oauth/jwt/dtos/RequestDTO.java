package com.spring3.oauth.jwt.dtos;

import lombok.Data;

@Data
public class RequestDTO {
    private Long requesterId;
    private double currentLatitude;
    private double currentLongitude;
    private int totalPintsDonated;
}
