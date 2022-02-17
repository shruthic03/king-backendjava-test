package com.king.java.backend.test.javatest.service;

import com.king.java.backend.test.javatest.model.User;
import lombok.NoArgsConstructor;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

@NoArgsConstructor
public class UserScoreManagement {

    private ConcurrentHashMap<Integer, ConcurrentSkipListSet<User>> userScores;

    public synchronized void saveUserScorePerLevel(final Integer levelId,final User userScore){

        ConcurrentSkipListSet<User> userScoreLevelSkipListSet = userScores.get(levelId);
        if(userScoreLevelSkipListSet!=null){
            userScoreLevelSkipListSet.add(userScore);
            userScores.replace(levelId, userScoreLevelSkipListSet);
        }else {
            userScoreLevelSkipListSet = new ConcurrentSkipListSet<>();
            userScoreLevelSkipListSet.add(userScore);
            userScores.putIfAbsent(levelId, userScoreLevelSkipListSet);
        }


    }
}
