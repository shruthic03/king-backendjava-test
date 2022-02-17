package com.king.java.backend.test.javatest.service;

import com.king.java.backend.test.javatest.configurations.LoginService;
import com.king.java.backend.test.javatest.model.User;
import com.king.java.backend.test.javatest.model.UserScore;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.reflect.Whitebox;

import java.util.Collections;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.*;
@RunWith(MockitoJUnitRunner.class)
@PrepareForTest({LoginService.class, ScoreService.class, ConcurrentHashMap.class})
public class ScoreServiceTest {

    ScoreService scoreService;

    @Before
    public void setUp() throws Exception
    {
        scoreService = new ScoreService(new LoginService(11, 11, 8, 10),15);
    }

    @Test
    public void getHighScoreList()
    {
        SortedSet<UserScore> userScores = Collections.synchronizedSortedSet(new TreeSet<>());
        userScores.add(new UserScore(new User("101"), 1100));
        userScores.add(new UserScore(new User("102"), 2100));
        userScores.add(new UserScore(new User("103"), 2100));
        userScores.add(new UserScore(new User("103"), 3300));

        Map<String, SortedSet<UserScore>> levelHighScoresMap = new ConcurrentHashMap<>();
        levelHighScoresMap.put("2", userScores);

        Whitebox.setInternalState(scoreService, "levelHighScoresMap", levelHighScoresMap);
        String scores = scoreService.getHighScoreList("2");
        assertEquals("103=3300,102=2100,101=1100", scores);
    }
}