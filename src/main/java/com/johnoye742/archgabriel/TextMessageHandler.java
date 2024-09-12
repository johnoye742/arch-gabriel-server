package com.johnoye742.archgabriel;

import com.google.gson.Gson;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TextMessageHandler extends TextWebSocketHandler {

    // A set of all connected clients
    Map<String, Set<WebSocketSession>> clients = new ConcurrentHashMap<>();
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        // After connection is established add the session to the clients set
        session.getUri();
    }
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        Gson gson = new Gson();
        JsonData data = gson.fromJson(message.getPayload(), JsonData.class);
        System.out.println(message.getPayload());

        if(data.getType().equals("join")) {
            clients.computeIfAbsent(data.getRoomId(), k -> new HashSet<>()).add(session);
        }

        if(data.getType().equals("message")) {
            Set<WebSocketSession> connectedClients = clients.get(data.getRoomId());
            // Do not send if not a member of the room
            if(connectedClients.contains(session)) {
                for(WebSocketSession ses : connectedClients) {
                    if(ses.isOpen())
                        ses.sendMessage(message);
                }
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {

    }
}
