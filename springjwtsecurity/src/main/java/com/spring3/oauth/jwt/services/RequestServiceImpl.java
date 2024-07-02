package com.spring3.oauth.jwt.services;

import com.spring3.oauth.jwt.models.DonorInfo;
import com.spring3.oauth.jwt.models.MemberInfo;
import com.spring3.oauth.jwt.models.Request;
import com.spring3.oauth.jwt.models.RequesterInfo;
import com.spring3.oauth.jwt.repositories.DonorRepository;
import com.spring3.oauth.jwt.repositories.MemberRepository;
import com.spring3.oauth.jwt.repositories.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public void sendRequest(Long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        double currentRadius = 5.0;
        boolean requestFulfilled = false;

        while (!requestFulfilled && currentRadius <= 100) {
            List<MemberInfo> potentialDonors = memberRepository.findByBloodGroupAndLocation(
                    request.getRequester().getBloodGroup(), request.getCurrentLatitude(), request.getCurrentLongitude(), currentRadius);

            for (MemberInfo donor : potentialDonors) {
                notificationService.sendRequestNotification(donor, request);
            }

            requestFulfilled = checkRequestFulfilled(request);

            if (!requestFulfilled) {
                currentRadius += 10.0;
            }
        }

        if (!requestFulfilled) {
            System.out.println("Request ID: " + request.getId() + " could not be fulfilled within 100 km radius.");
        }
    }

    @Override
    @Transactional
    public void handleDonorResponse(Long requestId, Long donorId, boolean accepted) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (accepted) {
            DonorInfo donorInfo = donorRepository.findById(donorId)
                    .orElseThrow(() -> new RuntimeException("Donor not found"));

            request.setDonorInfo(donorInfo);
            request.setTotalPintsDonated(request.getTotalPintsDonated() + 1);

            if (checkRequestFulfilled(request)) {
                request.setDisabled(true);
            }

            requestRepository.save(request);
        }
    }

    @Override
    @Transactional
    public void createRequest(Request request) {
        requestRepository.save(request);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Request> getRequestsByMemberId(Long memberId) {
        MemberInfo memberInfo = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        return requestRepository.findByRequester(memberInfo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Request> getAllRequests() {
        return requestRepository.findAll();
    }

    private boolean checkRequestFulfilled(Request request) {
        return request.getTotalPintsDonated() >= request.getRequester().getPints();
    }
}
