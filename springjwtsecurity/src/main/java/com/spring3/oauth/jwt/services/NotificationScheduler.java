package com.spring3.oauth.jwt.services;

import com.spring3.oauth.jwt.helpers.RequestWebSocketHandler;
import com.spring3.oauth.jwt.models.DonorInfo;
import com.spring3.oauth.jwt.models.Request;
import com.spring3.oauth.jwt.models.RequesterInfo;
import com.spring3.oauth.jwt.repositories.RequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NotificationScheduler {

    private static final Logger logger = LoggerFactory.getLogger(NotificationScheduler.class);

    @Autowired
    private RequestWebSocketHandler webSocketHandler;

    @Autowired
    private RequestRepository requestRepository;

    @Scheduled(fixedRate = 30000)
    public void sendNotifications() {
        try {
            List<Request> requests = requestRepository.findAll();
            for (Request requester : requests) {
                int remainingPints = requester.getTotalPintsDonated();
                if(remainingPints>0){
                DonorInfo donorInfo = requester.getDonorInfo();
                String memberId = donorInfo.getId().toString();
                RequesterInfo requesterInfo = requester.getRequester();
                String name = requesterInfo.getName();
                String bloodGroup = requesterInfo.getBloodGroup();

                String message = "Urgent " + bloodGroup + " blood needed, " + name;
                logger.info("Sending notification to {}: {}", name, message);

                webSocketHandler.sendNotificationToUser(memberId, message);}
            }
        } catch (Exception e) {
            logger.error("Error occurred while sending notifications", e);
        }
    }
}
