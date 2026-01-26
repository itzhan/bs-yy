package com.ws;

import cn.hutool.core.util.StrUtil;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/ws")
@Component
public class WebSocket {
    private static final ConcurrentHashMap<String, WebSocket> webSocketMap = new ConcurrentHashMap<>();

    private String fromUserId;
    private String toUserId;
    private Session session;

    public static void sendMessage(String message, String fromUserId, String toUserId) {
        if (toUserId == null || toUserId.isEmpty()) {
            for (Map.Entry<String, WebSocket> entry : webSocketMap.entrySet()) {
                try {
                    if (!entry.getKey().equals(fromUserId)) {
                        entry.getValue().session.getBasicRemote().sendText(message);
                    }
                } catch (IOException e) {
                    // ignore
                }
            }
        } else {
            WebSocket webSocket = webSocketMap.get(toUserId);
            if (webSocket != null) {
                try {
                    webSocket.session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        Map<String, String> param = parseQueryString(session.getQueryString());
        this.fromUserId = param.getOrDefault("user_id", "");
        this.toUserId = param.getOrDefault("to_id", "");
        this.session = session;
        if (!this.fromUserId.isEmpty()) {
            webSocketMap.put(this.fromUserId, this);
        }
    }

    @OnClose
    public void onClose(Session session) {
        Map<String, String> param = parseQueryString(session.getQueryString());
        String from = param.getOrDefault("user_id", "");
        if (StrUtil.isNotBlank(from)) {
            webSocketMap.remove(from);
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        // no-op
    }

    @OnMessage
    public void onMessage(String message) {
        if ("ping".equalsIgnoreCase(message)) {
            sendMessage("pong", fromUserId, toUserId);
        } else {
            sendMessage(message, fromUserId, toUserId);
        }
    }

    private Map<String, String> parseQueryString(String queryString) {
        Map<String, String> queryPairs = new HashMap<>();
        if (queryString == null || queryString.isEmpty()) return queryPairs;
        String[] pairs = queryString.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf('=');
            String key = (idx > 0) ? decode(pair.substring(0, idx)) : pair;
            String value = (idx > 0 && pair.length() > idx + 1) ? decode(pair.substring(idx + 1)) : null;
            queryPairs.put(key, value);
        }
        return queryPairs;
    }

    private String decode(String s) {
        try {
            return URLDecoder.decode(s, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return s;
        }
    }
}

