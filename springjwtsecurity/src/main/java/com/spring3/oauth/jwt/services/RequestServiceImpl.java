package com.spring3.oauth.jwt.services;

import com.spring3.oauth.jwt.helpers.RequestWebSocketHandler;
import com.spring3.oauth.jwt.models.DonorInfo;
import com.spring3.oauth.jwt.models.MemberInfo;
import com.spring3.oauth.jwt.models.Request;
import com.spring3.oauth.jwt.models.RequesterInfo;
import com.spring3.oauth.jwt.repositories.DonorRepository;
import com.spring3.oauth.jwt.repositories.MemberRepository;
import com.spring3.oauth.jwt.repositories.RequestRepository;
import com.spring3.oauth.jwt.repositories.RequesterRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RequestServiceImpl implements RequestService {

    @Autowired
    private RequesterRepository requesterRepository;
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private DonorRepository donorRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RequestWebSocketHandler webSocketHandler;

    @Override
    @Transactional
    public void sendRequest(Long requesterId) {
        RequesterInfo requester = requesterRepository.findById(requesterId)
                .orElseThrow(() -> new EntityNotFoundException("Requester not found with id: " + requesterId));


        double currentRadius = 15.0;
        boolean requestFulfilled = false;

      //  while (!requestFulfilled && currentRadius <= 100) {
            List<MemberInfo> potentialDonors = memberRepository.findByBloodGroupAndLocation(
                    requester.getBloodGroup(), requester.getLatitude(), requester.getLongitude(), currentRadius);

            for (MemberInfo donor : potentialDonors) {
                System.out.println(donor.getId());
               // notificationService.sendRequestNotification(donor, request);
                Long donorId = donor.getId();
                int pints = requester.getPints();
                Double lat = memberRepository.getlat(donor.getId());
                Double lng = memberRepository.getlon(donor.getId());
                requestRepository.insert(requesterId,donorId,pints,lat,lng);


            }

          //  requestFulfilled = checkRequestFulfilled(requester);

          //  if (!requestFulfilled) {
                currentRadius += 10.0;
           // }
      //  }

//        if (!requestFulfilled) {
//            System.out.println("Request ID: " + requester.getId() + " could not be fulfilled within 100 km radius.");
//        }
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
        System.out.println("Request created: " + request);
        webSocketHandler.sendNotification("Uregent Blood needed for " + request.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Request> getRequestsByMemberId(Long memberId) {
        // Implement get requests by member ID logic
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Request> getAllRequests() {
        return requestRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Request> getSentRequestsByMemberId(Long memberId) {
        return requestRepository.findByRequesterId(memberId);
    }

    private boolean checkRequestFulfilled(Request request) {
        return request.getTotalPintsDonated() >= request.getRequester().getPints();
    }
}