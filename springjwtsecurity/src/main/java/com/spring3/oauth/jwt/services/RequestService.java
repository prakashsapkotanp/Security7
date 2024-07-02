package com.spring3.oauth.jwt.services;

import com.spring3.oauth.jwt.models.Request;

public interface RequestService {
    void sendRequest(Long requestId);
    void handleDonorResponse(Long requestId, Long donorId, boolean accepted);
}
