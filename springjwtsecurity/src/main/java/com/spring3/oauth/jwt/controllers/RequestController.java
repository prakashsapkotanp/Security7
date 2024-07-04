package com.spring3.oauth.jwt.controllers;

import com.spring3.oauth.jwt.models.Request;
import com.spring3.oauth.jwt.services.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
public class RequestController {

    @Autowired
    private RequestService requestService;

    /**
     * Endpoint to send a request by its ID.
     * @param requestId ID of the request to be sent.
     * @return Response indicating the success or failure of the operation.
     */
    @PostMapping("/send/{requestId}")
    public ResponseEntity<String> sendRequest(@PathVariable Long requestId) {
        requestService.sendRequest(requestId);
        return ResponseEntity.ok("Request sent successfully");
    }

    /**
     * Endpoint to handle donor response to a request.
     * @param requestId ID of the request.
     * @param donorId ID of the donor.
     * @param accepted Boolean indicating whether the donor accepted the request.
     * @return Response indicating the success or failure of the operation.
     */
    @PostMapping("/{requestId}/respond")
    public ResponseEntity<String> handleDonorResponse(@PathVariable Long requestId,
                                                      @RequestParam Long donorId,
                                                      @RequestParam boolean accepted) {
        requestService.handleDonorResponse(requestId, donorId, accepted);
        return ResponseEntity.ok("Response handled successfully");
    }

    /**
     * Endpoint to create a new request.
     * @param request The request object to be created.
     * @return Response indicating the success or failure of the operation.
     */
    @PostMapping("/create")
    public ResponseEntity<String> createRequest(@RequestBody Request request) {
        requestService.createRequest(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Request created successfully");
    }

    /**
     * Endpoint to get all requests associated with a specific member.
     * @param memberId ID of the member.
     * @return List of requests associated with the member.
     */
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<Request>> getRequestsByMemberId(@PathVariable Long memberId) {
        List<Request> requests = requestService.getRequestsByMemberId(memberId);
        return ResponseEntity.ok(requests);
    }

    /**
     * Endpoint to get all requests.
     * @return List of all requests.
     */
    @GetMapping
    public ResponseEntity<List<Request>> getAllRequests() {
        List<Request> requests = requestService.getAllRequests();
        return ResponseEntity.ok(requests);
    }

    /**
     * Endpoint to get all requests sent by a specific member.
     * @param memberId ID of the member.
     * @return List of requests sent by the member.
     */
    @GetMapping("/sent/member/{memberId}")
    public ResponseEntity<List<Request>> getSentRequestsByMemberId(@PathVariable Long memberId) {
        List<Request> requests = requestService.getSentRequestsByMemberId(memberId);
        return ResponseEntity.ok(requests);
    }
}
