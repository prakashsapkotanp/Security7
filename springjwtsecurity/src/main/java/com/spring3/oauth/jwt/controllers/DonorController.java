package com.spring3.oauth.jwt.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.spring3.oauth.jwt.models.DonorInfo;
import com.spring3.oauth.jwt.services.DonorService;

@RestController
@RequestMapping("/api/v1/donor-infos")
public class DonorController {
    
    private final DonorService donorService;

    public DonorController(DonorService donorService) {
        this.donorService = donorService;
    }

    @GetMapping
    public ResponseEntity<List<DonorInfo>> getAllDonorInfos() {
        List<DonorInfo> donorInfos = donorService.getAllDonorInfos();
        return new ResponseEntity<>(donorInfos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DonorInfo> getDonorInfoById(@PathVariable Long id) {
        Optional<DonorInfo> donorInfo = donorService.getDonorInfoById(id);
        return donorInfo.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<DonorInfo> saveDonorInfo(@RequestBody DonorInfo donorInfo) {
        DonorInfo savedDonorInfo = donorService.saveDonorInfo(donorInfo);
        return new ResponseEntity<>(savedDonorInfo, HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<DonorInfo> updateDonorInfo(@PathVariable Long id, @RequestBody DonorInfo donorInfo) {
        DonorInfo updatedDonorInfo = donorService.updateDonorInfo(id, donorInfo);
        return new ResponseEntity<>(updatedDonorInfo, HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDonorInfo(@PathVariable Long id) {
        donorService.deleteDonorInfo(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //  endpoint to search DonorInfo by MemberInfo id
    @GetMapping("/by-member-id/{memberId}")
    public ResponseEntity<DonorInfo> getDonorInfoByMemberId(@PathVariable Long memberId) {
        Optional<DonorInfo> donorInfo = donorService.getDonorInfoByMemberId(memberId);
        return donorInfo.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
