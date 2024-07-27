package com.spring3.oauth.jwt.services;

import com.spring3.oauth.jwt.helpers.RequestWebSocketHandler;
import com.spring3.oauth.jwt.models.Request;
import com.spring3.oauth.jwt.repositories.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestServiceImpl implements RequestService {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private RequestWebSocketHandler webSocketHandler;

    @Override
    public void sendRequest(Long requestId) {
        // Implement send request logic
    }

    @Override
    public void handleDonorResponse(Long requestId, Long donorId, boolean accepted) {
        // Implement handle donor response logic
    }

    @Override
    public void createRequest(Request request) {
        requestRepository.save(request);
        System.out.println("Request created: " + request);
        webSocketHandler.sendNotification("Uregent Blood needed for " + request.getId());
    }

    @Override
    public List<Request> getRequestsByMemberId(Long memberId) {
        // Implement get requests by member ID logic
        return null;
    }

    @Override
    public List<Request> getAllRequests() {
        // Implement get all requests logic
        return null;
    }

    @Override
    public List<Request> getSentRequestsByMemberId(Long memberId) {
        // Implement get sent requests by member ID logic
        return null;
    }
}
