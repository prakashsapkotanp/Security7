package com.spring3.oauth.jwt.tasks;

import com.spring3.oauth.jwt.models.Request;
import com.spring3.oauth.jwt.repositories.RequestRepository;
import com.spring3.oauth.jwt.services.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduledTasks {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private RequestService requestService;

    @Scheduled(fixedRate = 600000) // Every 10 minutes
    public void increaseRequestRadius() {
        List<Request> requests = requestRepository.findByCurrentRadiusLessThanEqual(100);
        for (Request request : requests) {
            request.setCurrentRadius(request.getCurrentRadius() + 10);
            requestRepository.save(request);
            requestService.sendRequestToNearbyDonors(request);
        }
    }
}
