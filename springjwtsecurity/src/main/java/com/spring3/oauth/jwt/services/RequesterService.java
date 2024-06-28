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

    public void deleteRequester(Long id) {
        requesterRepository.deleteById(id);
    }


}
