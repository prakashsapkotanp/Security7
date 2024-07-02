package com.spring3.oauth.jwt.services;

import com.spring3.oauth.jwt.models.MemberInfo;
import com.spring3.oauth.jwt.models.Request;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    public void sendRequestNotification(MemberInfo donor, Request request) {
        // Implement actual notification logic here, e.g., sending an email or SMS
        System.out.println("Notification sent to donor: " + donor.getFirstname() + " for request ID: " + request.getId());
    }
}
