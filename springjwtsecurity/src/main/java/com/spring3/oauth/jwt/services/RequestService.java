package com.spring3.oauth.jwt.services;

import com.spring3.oauth.jwt.models.DonorInfo;
import com.spring3.oauth.jwt.models.MemberLocation;
import com.spring3.oauth.jwt.models.Request;
import com.spring3.oauth.jwt.models.RequesterInfo;
import com.spring3.oauth.jwt.repositories.DonorRepository;
import com.spring3.oauth.jwt.repositories.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequestService {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private DonorRepository donorRepository;

    @Transactional
    public void createRequest(RequesterInfo requester) {
        Request request = new Request();
        request.setRequester(requester);
        request.setCurrentRadius(5);
        request.setCreatedAt(LocalDateTime.now());
        requestRepository.save(request);
        sendRequestToNearbyDonors(request);
    }

    public void sendRequestToNearbyDonors(Request request) {
        List<DonorInfo> nearbyDonors = new ArrayList<>();
        double radius = request.getCurrentRadius();
        while (radius <= 100 && nearbyDonors.isEmpty()) {
            nearbyDonors = findNearbyDonors(request.getRequester().getMemberLocation(), radius);
            radius += 10;
        }

        // If no donors found within 100km, get all donors
        if (nearbyDonors.isEmpty()) {
            nearbyDonors = donorRepository.findAll().stream()
                    .filter(this::isEligibleForDonation)
                    .collect(Collectors.toList());
        }

        for (DonorInfo donor : nearbyDonors) {
            if (donor.isStatus()) {
                // Simulate donor accepting the request
                handleDonorResponse(donor, request, true);
                return;
            }
        }
        // No donor accepted, schedule to increase radius
    }

    public List<DonorInfo> findNearbyDonors(MemberLocation requesterLocation, double radius) {
        return donorRepository.findAll().stream()
                .filter(donor -> calculateDistance(requesterLocation, donor.getMemberLocation()) <= radius)
                .filter(donor -> isEligibleForDonation(donor))
                .sorted((d1, d2) -> Double.compare(calculateDistance(requesterLocation, d1.getMemberLocation()), calculateDistance(requesterLocation, d2.getMemberLocation())))
                .collect(Collectors.toList());
    }

    public void handleDonorResponse(DonorInfo donor, Request request, boolean accepted) {
        if (accepted) {
            requestRepository.delete(request);
            donor.setStatus(false);
            donorRepository.save(donor);
        }
    }

    private boolean isEligibleForDonation(DonorInfo donor) {
        LocalDate lastDonation = donor.getMemberInfo().getLastTimeOfDonation().toLocalDate();
        return lastDonation.plusDays(90).isBefore(LocalDate.now());
    }

    private double calculateDistance(MemberLocation loc1, MemberLocation loc2) {
        // Haversine formula
        double R = 6371; // Radius of the earth in km
        double dLat = Math.toRadians(loc2.getLatitude() - loc1.getLatitude());
        double dLon = Math.toRadians(loc2.getLongitude() - loc1.getLongitude());
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(loc1.getLatitude())) * Math.cos(Math.toRadians(loc2.getLatitude()))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // Distance in km
    }
}
