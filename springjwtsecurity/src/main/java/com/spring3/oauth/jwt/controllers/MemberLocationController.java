package com.spring3.oauth.jwt.controllers;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.spring3.oauth.jwt.models.MemberLocation;
import com.spring3.oauth.jwt.services.MemberLocationService;

@RestController
@RequestMapping("/api/v1/member-locations")
public class MemberLocationController {
    private final MemberLocationService memberLocationService;

    public MemberLocationController(MemberLocationService memberLocationService) {
        this.memberLocationService = memberLocationService;
    }

    @GetMapping
    public ResponseEntity<List<MemberLocation>> getAllMemberLocations() {
        List<MemberLocation> memberLocations = memberLocationService.getAllMemberLocations();
        return ResponseEntity.ok(memberLocations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberLocation> getMemberLocationById(@PathVariable Long id) {
        return memberLocationService.getMemberLocationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MemberLocation> saveMemberLocation(@RequestBody MemberLocation memberLocation) {
        MemberLocation savedLocation = memberLocationService.saveMemberLocation(memberLocation);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedLocation);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MemberLocation> updateMemberLocation(@PathVariable Long id, @RequestBody MemberLocation memberLocation) {
        Optional<MemberLocation> existingLocationOptional = memberLocationService.getMemberLocationById(id);
        if (existingLocationOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        MemberLocation existingLocation = existingLocationOptional.get();
        memberLocation.setId(id); // Ensure the provided ID matches the path variable ID
        MemberLocation updatedLocation = memberLocationService.saveMemberLocation(memberLocation);
        return ResponseEntity.ok(updatedLocation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMemberLocation(@PathVariable Long id) {
        memberLocationService.deleteMemberLocation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/nearest")
    public ResponseEntity<List<MemberLocation>> getNearestMemberLocations(
            @RequestParam("latitude") double latitude,
            @RequestParam("longitude") double longitude,
            @RequestParam("radius") double radius
    ) {
        List<MemberLocation> allMemberLocations = memberLocationService.getAllMemberLocations();
        
        // Calculate distances and filter users within specified radius
        List<MemberLocation> nearestMemberLocations = allMemberLocations.stream()
            .filter(memberLocation -> calculateDistance(latitude, longitude, memberLocation.getLatitude(), memberLocation.getLongitude()) <= radius)
            .collect(Collectors.toList());
        
        // Sort the nearest locations based on distance in ascending order
        nearestMemberLocations.sort(Comparator.comparingDouble(memberLocation -> calculateDistance(latitude, longitude, memberLocation.getLatitude(), memberLocation.getLongitude())));
        
        return ResponseEntity.ok(nearestMemberLocations);
    }

    // Helper method to calculate distance between two locations using Haversine formula
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double earthRadius = 6371; // in kilometers

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return earthRadius * c;
    }
}
