package com.spring3.oauth.jwt.services;

import com.spring3.oauth.jwt.models.Request;

import java.util.List;

public interface RequestService {
    void sendRequest(Long requestId);
    void handleDonorResponse(Long requestId, Long donorId, boolean accepted);
    void createRequest(Request request);
    List<Request> getRequestsByMemberId(Long memberId);
    List<Request> getAllRequests();
    List<Request> getSentRequestsByMemberId(Long memberId);
}