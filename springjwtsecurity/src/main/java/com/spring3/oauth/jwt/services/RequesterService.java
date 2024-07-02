package com.spring3.oauth.jwt.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.spring3.oauth.jwt.models.RequesterInfo;
import com.spring3.oauth.jwt.repositories.RequesterRepository;


@Service
public class RequesterService {

    private final RequesterRepository requesterRepository;

    public RequesterService(RequesterRepository requesterRepository) {
        this.requesterRepository = requesterRepository;
    }

    public List<RequesterInfo> getAllRequesters() {
        return requesterRepository.findAll();
    }

    public Optional<RequesterInfo> getRequesterById(Long id) {
        return requesterRepository.findById(id);
    }

    public List<RequesterInfo> getRequestersByBloodGroup(String bloodGroup) {
        return requesterRepository.findByBloodGroup(bloodGroup);
    }

    public List<RequesterInfo> getRequestersByLocation(double latitude, double longitude) {
        return requesterRepository.findByLatitudeAndLongitude(latitude, longitude);
    }

    public List<RequesterInfo> getRequestersByName(String name) {
        return requesterRepository.findByName(name);
    }

    public RequesterInfo saveRequester(RequesterInfo requesterInfo) {
        return requesterRepository.save(requesterInfo);
    }

    public Optional<RequesterInfo> updateRequester(Long id, RequesterInfo requesterInfoToUpdate) {
        Optional<RequesterInfo> existingRequester = requesterRepository.findById(id);
        if (existingRequester.isPresent()) {
            RequesterInfo currentRequester = existingRequester.get();

            // Update fields with non-null values from requesterInfoToUpdate
            if (requesterInfoToUpdate.getBloodGroup() != null) {
                currentRequester.setBloodGroup(requesterInfoToUpdate.getBloodGroup());
            }
            if (requesterInfoToUpdate.getPints() != 0) {
                currentRequester.setPints(requesterInfoToUpdate.getPints());
            }
            if (requesterInfoToUpdate.getLatitude() != 0.0 && requesterInfoToUpdate.getLongitude() != 0.0) {
                currentRequester.setLatitude(requesterInfoToUpdate.getLatitude());
                currentRequester.setLongitude(requesterInfoToUpdate.getLongitude());
            }
            if (requesterInfoToUpdate.getName() != null) {
                currentRequester.setName(requesterInfoToUpdate.getName());
            }

            // Save and return updated requester
            try {
                RequesterInfo updatedRequester = requesterRepository.save(currentRequester);
                return Optional.of(updatedRequester);
            } catch (Exception e) {
                // Handle database errors or constraints
                e.printStackTrace();
                // You can log the error or throw custom exceptions
                throw new RuntimeException("Failed to update requester with id: " + id);
            }
        } else {
            // Return empty optional if requester with given id does not exist
            return Optional.empty();
        }
    }
    public void deleteRequester(Long id) {
        requesterRepository.deleteById(id);
    }


}
