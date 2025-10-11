package com.example.catering_system.service;

import com.example.catering_system.model.User;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple in-memory session manager using the Singleton pattern.
 * For production, prefer Spring Security sessions or a distributed store.
 */
public final class SessionManager {

    private static volatile SessionManager instance;

    private final Map<String, User> sessionIdToUser;

    private SessionManager() {
        this.sessionIdToUser = new ConcurrentHashMap<>();
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            synchronized (SessionManager.class) {
                if (instance == null) {
                    instance = new SessionManager();
                }
            }
        }
        return instance;
    }

    public String createSession(User user) {
        String sessionId = UUID.randomUUID().toString();
        sessionIdToUser.put(sessionId, user);
        return sessionId;
    }

    public User getUser(String sessionId) {
        return sessionIdToUser.get(sessionId);
    }

    public void invalidate(String sessionId) {
        sessionIdToUser.remove(sessionId);
    }
}


