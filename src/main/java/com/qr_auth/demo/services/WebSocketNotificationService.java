package com.qr_auth.demo.services;


import com.qr_auth.demo.Classes.SessionCours;
import com.qr_auth.demo.Classes.scan_user;
import com.qr_auth.demo.Classes.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class WebSocketNotificationService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void notifyUserChanges(Utilisateur utilisateur, String action) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("user", utilisateur);
        payload.put("action", action);  // "CREATE", "UPDATE", "DELETE"

        messagingTemplate.convertAndSend("/topic/user-changes", payload);
    }

    public void notifySessionChanges(SessionCours session, String action) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("session", session);
        payload.put("action", action);

        messagingTemplate.convertAndSend("/topic/session-changes", payload);
    }

    public void notifyScanChanges(scan_user scan, String action) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("scan", scan);
        payload.put("action", action);
        messagingTemplate.convertAndSend("/topic/scan-changes", payload);
    }


}