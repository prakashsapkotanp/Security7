package com.spring3.oauth.jwt.controllers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spring3.oauth.jwt.dtos.RequestDTO;
import com.spring3.oauth.jwt.models.DonorInfo;
import com.spring3.oauth.jwt.models.MemberInfo;
import com.spring3.oauth.jwt.models.Request;
import com.spring3.oauth.jwt.models.RequesterInfo;
import com.spring3.oauth.jwt.repositories.RequestRepository;
import com.spring3.oauth.jwt.services.DonorService;
import com.spring3.oauth.jwt.services.RequestService;
import com.spring3.oauth.jwt.services.RequesterService;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
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
    @Autowired
    private RequestRepository requestRepository;

    /**
     * Endpoint to send a request by its ID.
     *
     * @param requesterId ID of the request to be sent.
     * @return Response indicating the success or failure of the operation.
     */
    @PostMapping("/send/{requesterId}")
    public MemberInfo sendRequest(@PathVariable Long requesterId) {
        requestService.sendRequest(requesterId);
        MemberInfo memberInfo = new MemberInfo();
        return memberInfo;
    }

    /**
     * Endpoint to handle donor response to a request.
     *
     * @param requestId ID of the request.
     * @param donorId   ID of the donor.
     * @param accepted  Boolean indicating whether the donor accepted the request.
     * @return Response indicating the success or failure of the operation.
     */
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

    /**
     * Endpoint to get all requests associated with a specific member.
     *
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
     *
     * @return List of all requests.
     */
    @GetMapping
    public ResponseEntity<List<Request>> getMannualquests() {
        List<Request> requests = requestRepository.manualFindAll();
        return ResponseEntity.ok(requests);
    }
    @GetMapping("/getRequestByRequesterId/{requesterId}")
    public ResponseEntity<List<Request>> getMannuaRequestByRequesterId(@PathVariable Long requesterId) {
        List<Request> requests = requestRepository.findByRequesterId(requesterId);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/sent/{memberId}")
    public ResponseEntity<List<Request>> getSentRequestsByMemberId(@PathVariable Long memberId) {
        List<Request> requests = requestService.getSentRequestsByMemberId(memberId);
        return ResponseEntity.ok(requests);
    }
}
