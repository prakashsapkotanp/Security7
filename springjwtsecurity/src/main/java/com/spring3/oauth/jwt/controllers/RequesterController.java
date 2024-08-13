package com.spring3.oauth.jwt.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.spring3.oauth.jwt.models.RequesterInfo;
import com.spring3.oauth.jwt.services.RequesterService;

@RestController
@RequestMapping("/api/v1/requesters")
public class RequesterController {
    private final RequesterService requesterService;

    public RequesterController(RequesterService requesterService) {
        this.requesterService = requesterService;
    }

    @GetMapping
    public List<RequesterInfo> getAllRequesters() {
        return requesterService.getAllRequesters();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RequesterInfo> getRequesterById(@PathVariable Long id) {
        Optional<RequesterInfo> requesterInfo = requesterService.getRequesterById(id);
        return requesterInfo.map(info -> new ResponseEntity<>(info, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/bloodgroup/{bloodGroup}")
    public List<RequesterInfo> getRequestersByBloodGroup(@PathVariable String bloodGroup) {
        return requesterService.getRequestersByBloodGroup(bloodGroup);
    }

    @GetMapping("/location/{latitude}/{longitude}")
    public List<RequesterInfo> getRequestersByLocation(@PathVariable double latitude, @PathVariable double longitude) {
        return requesterService.getRequestersByLocation(latitude, longitude);
    }

    @GetMapping("/name/{name}")
    public List<RequesterInfo> getRequestersByPhone(@PathVariable String name) {
        return requesterService.getRequestersByName(name);
    }
    @PostMapping
    public ResponseEntity<RequesterInfo> saveRequester(@RequestBody RequesterInfo requesterInfo) {
        RequesterInfo savedRequester = requesterService.saveRequester(requesterInfo);
        return new ResponseEntity<>(savedRequester, HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<RequesterInfo> updateRequester(@PathVariable Long id, @RequestBody RequesterInfo requesterInfoToUpdate) {
        Optional<RequesterInfo> existingRequester = requesterService.getRequesterById(id);
        if (existingRequester.isPresent()) {
            RequesterInfo currentRequester = existingRequester.get();
            // Copy new data to the existing entity
            BeanUtils.copyProperties(requesterInfoToUpdate, currentRequester, "id");
            RequesterInfo updatedRequester = requesterService.saveRequester(currentRequester);
            return new ResponseEntity<>(updatedRequester, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequester(@PathVariable Long id) {
        requesterService.deleteRequester(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}