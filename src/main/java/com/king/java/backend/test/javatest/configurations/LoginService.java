package com.king.java.backend.test.javatest.configurations;

import com.king.java.backend.test.javatest.model.SessionInfo;
import com.king.java.backend.test.javatest.model.User;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LoginService {
    private final Map<String, SessionInfo> sessionInfoMap = new ConcurrentHashMap<>();



    private final int sessionIdLength;
    private final int sessionTimeOutMins;

    private static SecureRandom random = new SecureRandom();


    public LoginService(int initialDelay, int delay, int sessionIdLength, int sessionTimeOutMins) {
        this.sessionIdLength = sessionIdLength;
        this.sessionTimeOutMins = sessionTimeOutMins;

        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleWithFixedDelay(() -> cleanup(), initialDelay, delay, TimeUnit.MINUTES);
    }


    public String doLogin(String userId) {
        User user = new User(userId);
        return createUserSession(user).getSessionId();
    }


    private SessionInfo createUserSession(User user) {
        // create session
        Instant timeStamp = Instant.now();
        String sessionId = generateNewSessionId();
        SessionInfo sessionInfo = new SessionInfo(sessionId, timeStamp, user);

        // set the session in the map
        sessionInfoMap.put(sessionInfo.getSessionId(), sessionInfo);

        return sessionInfo;
    }


    private String generateNewSessionId() {

        return new BigInteger(130, random).toString(32);

    }


    public boolean isSessionValid(SessionInfo sessionInfo) {

        return sessionInfo != null && Duration.between(sessionInfo.getTimestamp(), Instant.now()).toMinutes() <= sessionTimeOutMins;
    }


    public SessionInfo getSessionInfo(String sessionId) {
        return sessionInfoMap.get(sessionId);
    }


    private void cleanup() {
        sessionInfoMap.values().forEach(sessionInfo -> {
            if (isSessionValid(sessionInfo) == false) sessionInfoMap.remove(sessionInfo.getSessionId());
        });

    }
}
