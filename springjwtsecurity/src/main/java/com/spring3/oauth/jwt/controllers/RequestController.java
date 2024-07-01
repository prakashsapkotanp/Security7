package com.spring3.oauth.jwt.controllers;

import com.spring3.oauth.jwt.models.Request;
import com.spring3.oauth.jwt.models.RequesterInfo;
import com.spring3.oauth.jwt.repositories.RequestRepository;
import com.spring3.oauth.jwt.services.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
public class RequestController {

//    @Autowired
//    private RequestService requestService;
//
//    @PostMapping
//    public ResponseEntity<String> createRequest(@RequestBody RequesterInfo requester) {
//        requestService.createRequest(requester);
//        return ResponseEntity.ok("Request sent to nearby donors");
//    }
//    @PutMapping("/{id}")
//    public ResponseEntity<String> updateRequest(@PathVariable Long id, @RequestBody Request updatedRequest) {
//        requestService.updateRequest(id, updatedRequest);
//        return ResponseEntity.ok("Request updated successfully");
//    }

@Autowired
private RequestRepository requestRepository;

    @PostMapping
    public List<Request> getAllRequests() {
        return requestRepository.findAll();
    }
}
