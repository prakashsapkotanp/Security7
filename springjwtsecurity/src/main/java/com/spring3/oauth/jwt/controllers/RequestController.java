package com.spring3.oauth.jwt.controllers;

import com.spring3.oauth.jwt.dtos.RequestDTO;
import com.spring3.oauth.jwt.models.Request;
import com.spring3.oauth.jwt.models.RequesterInfo;
import com.spring3.oauth.jwt.services.DonorService;
import com.spring3.oauth.jwt.services.RequestService;
import com.spring3.oauth.jwt.services.RequesterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/requests")
public class RequestController {

    @Autowired
    private RequestService requestService;
    @Autowired
    private RequesterService requesterService;
    @Autowired
    private DonorService donorService;

    @PostMapping("/send/{requestId}")
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

    @PostMapping("/create")
    public ResponseEntity<String> createRequest(@RequestBody RequestDTO requestDTO) {
        Optional<RequesterInfo> requesterInfo = requesterService.getRequesterById(requestDTO.getRequesterId());
        // Optional<DonorInfo> donorInfo = donorService.getDonorInfoById(requestDTO.getDonorId());

        if (requesterInfo.isPresent()) {
            Request request = new Request(
                    requesterInfo.get(),
                    requestDTO.getCurrentLatitude(),
                    requestDTO.getCurrentLongitude(),
                    LocalDateTime.now(),
                    requestDTO.getTotalPintsDonated()
            );
            requestService.createRequest(request);
            return ResponseEntity.status(HttpStatus.CREATED).body("Request created successfully");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request failed to send");
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<Request>> getRequestsByMemberId(@PathVariable Long memberId) {
        List<Request> requests = requestService.getRequestsByMemberId(memberId);
        return ResponseEntity.ok(requests);
    }

    @GetMapping
    public ResponseEntity<List<Request>> getAllRequests() {
        List<Request> requests = requestService.getAllRequests();
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/sent/{memberId}")
    public ResponseEntity<List<Request>> getSentRequestsByMemberId(@PathVariable Long memberId) {
        List<Request> requests = requestService.getSentRequestsByMemberId(memberId);
        return ResponseEntity.ok(requests);
    }
}
