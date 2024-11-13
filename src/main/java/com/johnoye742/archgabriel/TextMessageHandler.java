package com.johnoye742.archgabriel;

import com.google.gson.Gson;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import redis.clients.jedis.UnifiedJedis;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextMessageHandler extends TextWebSocketHandler {

    // A set of all connected clients
    Map<String, Set<WebSocketSession>> clients = new ConcurrentHashMap<>();
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        // After connection is established add the session to the clients set
        System.out.println(session.getUri().getPath());
        clients.computeIfAbsent(Objects.requireNonNull(session.getUri()).getPath(), _ -> new HashSet<>()).add(session);
    }
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
    Gson gson = new Gson();
        JsonData data = gson.fromJson(message.getPayload(), JsonData.class);
        System.out.println(message.getPayload());

        if(data.getType().equals("message")) {
            Set<WebSocketSession> connectedClients = clients.get(Objects.requireNonNull(session.getUri()).getPath());
            // Do not send if not a member of the room
            try (UnifiedJedis redis = new UnifiedJedis("redis://localhost:6379")) {
                if (connectedClients.contains(session)) {
                    redis.lpush(Objects.requireNonNull(session.getUri()).getPath(), message.getPayload());
                    for (WebSocketSession ses : connectedClients) {
                        if (ses.isOpen()) {
                            ses.sendMessage(message);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {

    }
}
