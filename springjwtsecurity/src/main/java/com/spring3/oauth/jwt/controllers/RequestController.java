package com.spring3.oauth.jwt.controllers;

import com.spring3.oauth.jwt.services.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/requests")
public class RequestController {

    @Autowired
    private RequestService requestService;

    @PostMapping("/{requestId}/send")
    public ResponseEntity<String> sendRequest(@PathVariable Long requestId) {
        requestService.sendRequest(requestId);
        return ResponseEntity.ok("Request sent successfully");
    }

    @PostMapping("/{requestId}/respond")
    public ResponseEntity<String> handleDonorResponse(@PathVariable Long requestId,
                                                      @RequestParam Long donorId,
                                                      @RequestParam boolean accepted) {
        requestService.handleDonorResponse(requestId, donorId, accepted);
        return ResponseEntity.ok("Response handled successfully");
    }
}
