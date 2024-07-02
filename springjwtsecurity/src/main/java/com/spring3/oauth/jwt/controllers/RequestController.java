package com.spring3.oauth.jwt.controllers;

import com.spring3.oauth.jwt.models.Request;
import com.spring3.oauth.jwt.services.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/requests")
public class RequestController {

    @Autowired
    private RequestService requestService;

    @PostMapping("/send/{requestId}")
    public ResponseEntity<String> sendRequest(@PathVariable Long requestId) {
        try {
            requestService.sendRequest(requestId);
            return ResponseEntity.ok("Request sent successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/{requestId}/respond")
    public ResponseEntity<String> handleDonorResponse(@PathVariable Long requestId,
                                                      @RequestParam Long donorId,
                                                      @RequestParam boolean accepted) {
        try {
            requestService.handleDonorResponse(requestId, donorId, accepted);
            return ResponseEntity.ok("Response handled successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<String> createRequest(@RequestBody Request request) {
        try {
            requestService.createRequest(request);
            return ResponseEntity.status(HttpStatus.CREATED).body("Request created successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
}