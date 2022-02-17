package com.king.java.backend.test.javatest.service;

import com.king.java.backend.test.javatest.configurations.LoginService;
import com.king.java.backend.test.javatest.constants.LoginConstants;
import com.king.java.backend.test.javatest.constants.Mutex;
import com.king.java.backend.test.javatest.model.SessionInfo;
import com.king.java.backend.test.javatest.model.User;
import com.king.java.backend.test.javatest.model.UserScore;

import java.util.Collections;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;



public class ScoreService
{
    private final Mutex mutex;
    private final LoginService loginService;
    private final int priorityQueueCapacity;
    private final Map<String, ConcurrentHashMap<String, Integer>> userHighestScorePerLevel = new ConcurrentHashMap<>();
    private final Map<String, SortedSet<UserScore>> levelHighScoresMap = new ConcurrentHashMap<>();

    public ScoreService(LoginService service, int priorityQueueCapacity)
    {
        this.mutex = new Mutex();
        this.loginService = service;
        this.priorityQueueCapacity = priorityQueueCapacity;
    }


    public void postScore(String levelId, String sessionId, int score)
    {
        SessionInfo sessionInfo = loginService.getSessionInfo(sessionId);

        if (loginService.isSessionValid(sessionInfo))
        {
            String userId = sessionInfo.getUser().getUserID();
            SortedSet<UserScore> highScores;
            ConcurrentHashMap<String, Integer> playerHighScores;


            synchronized (mutex.getLock(levelId))
            {
                highScores = levelHighScoresMap.get(levelId);
                if (highScores == null)
                {
                    highScores = Collections.synchronizedSortedSet(new TreeSet<>());
                    levelHighScoresMap.put(levelId, highScores);
                }

                playerHighScores = userHighestScorePerLevel.get(levelId);

                if (playerHighScores == null)
                {
                    playerHighScores = new ConcurrentHashMap<>();
                    userHighestScorePerLevel.put(levelId, playerHighScores);
                }
            }


            int prevScore;
            synchronized (mutex.getLock(levelId, userId))
            {
                Integer userScore = playerHighScores.get(userId);
                prevScore = userScore == null ? -1 : userScore;
            }

            synchronized (mutex.getLock(levelId))
            {

                if (score > prevScore)
                {
                    playerHighScores.put(userId, score);
                    highScores.remove(new UserScore(new User(userId)));
                    boolean operationResult = highScores.add(new UserScore(new User(userId), score));

                       while(operationResult == false)
                        operationResult = highScores.add(new UserScore(new User(userId), score+ LoginConstants.NUDGE_FACTOR));

                    if (highScores.size() > priorityQueueCapacity)
                        highScores.remove(highScores.last());
                }
            }
        }
    }


    public String getHighScoreList(String level)
    {
        StringBuilder builder = new StringBuilder();
        SortedSet<UserScore> userScores = levelHighScoresMap.get(level);
        if (userScores==null || userScores.size() == 0)
        {
            return "";
        }
        else
        {
            userScores.forEach(userScore -> builder.append(userScore.getUser().getUserID()).append("=").append(Math.round(userScore.getScore())).append(","));
            builder.setLength(builder.length() - 1);
            return builder.toString();
        }
    }
}
