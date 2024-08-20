package com.spring3.oauth.jwt.helpers;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RequestWebSocketHandler extends TextWebSocketHandler {

    // Map to store sessions associated with their MemberIds
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Extract MemberId from the session (assuming it's passed as a query parameter)
        String memberId = session.getUri().getQuery().split("=")[1]; // Extracting memberId from URI query parameters
        sessions.put(memberId, session);
        System.out.println("WebSocket connection established for MemberId: " + memberId);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Handle incoming messages if needed
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // Remove session on close
        sessions.values().remove(session);
        System.out.println("WebSocket connection closed with session ID: " + session.getId());
    }

    public void sendNotificationToUser(String memberId, String message) {
        WebSocketSession session = sessions.get(memberId);
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(message));
                System.out.println("Notification sent to MemberId: " + memberId);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No active WebSocket session found for MemberId: " + memberId);
        }
    }
}
