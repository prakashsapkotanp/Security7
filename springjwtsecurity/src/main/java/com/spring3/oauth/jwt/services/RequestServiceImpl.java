package com.spring3.oauth.jwt.services.impl;

import com.spring3.oauth.jwt.models.*;
import com.spring3.oauth.jwt.repositories.*;
import com.spring3.oauth.jwt.services.NotificationService;
import com.spring3.oauth.jwt.services.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestServiceImpl implements RequestService {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private DonorRepository donorRepository;

    @Autowired
    private NotificationService notificationService;

    @Override
    public void sendRequest(Long requestId) {
        Request request = requestRepository.findById(requestId).orElseThrow(() -> new RuntimeException("Request not found"));
        double currentRadius = 5.0;
        boolean requestFulfilled = false;

        while (!requestFulfilled && currentRadius <= 100) {
            List<MemberInfo> potentialDonors = memberRepository.findByBloodGroupAndLocation(
                    request.getRequester().getBloodGroup(), request.getCurrentLatitude(), request.getCurrentLongitude(), currentRadius);

            for (MemberInfo donor : potentialDonors) {
                // Send request notification to donor
                notificationService.sendRequestNotification(donor, request);
            }

            // Simulate donor responses and check if the request is fulfilled
            requestFulfilled = checkRequestFulfilled(request);

            if (!requestFulfilled) {
                currentRadius += 10.0;
            }
        }

        if (!requestFulfilled) {
            // Mark the request as expired or take other actions
            System.out.println("Request ID: " + request.getId() + " could not be fulfilled within 100 km radius.");
        }
    }

    @Override
    public void handleDonorResponse(Long requestId, Long donorId, boolean accepted) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (accepted) {
            DonorInfo donorInfo = donorRepository.findById(donorId)
                    .orElseThrow(() -> new RuntimeException("Donor not found"));

            request.setDonorInfo(donorInfo);
            // Assuming each donor donates 1 pint, update the total pints donated
            request.setTotalPintsDonated(request.getTotalPintsDonated() + 1);

            // Check if the required pints are fulfilled and update the request
            if (checkRequestFulfilled(request)) {
                request.setDisabled(true);
            }
            requestRepository.save(request);
        }
    }

    private boolean checkRequestFulfilled(Request request) {
        return request.getTotalPintsDonated() >= request.getRequester().getPints();
    }
}
