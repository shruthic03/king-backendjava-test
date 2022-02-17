package com.king.java.backend.test.javatest.controller;

import com.king.java.backend.test.javatest.configurations.LoginService;
import com.king.java.backend.test.javatest.constants.LoginConstants;
import com.king.java.backend.test.javatest.service.ScoreService;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import static com.king.java.backend.test.javatest.constants.LoginConstants.port;

@RestController
public class KingMiniGameController {

    static LoginService loginService;
    static ScoreService scoreService;
    LoginConstants loginConstants;

    public static void startApp() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.setExecutor(Executors.newCachedThreadPool());


        HttpContext rootContext = server.createContext("/");
        //loginService = new LoginService(initialDelay, delay, sessionIdLength, sessionTimeOutMins);
        //ScoreService scoreService = new ScoreService(loginService, priorityQueueCapacity);



        loginService = new LoginService(LoginConstants.initialDelay, LoginConstants.delay, LoginConstants.sessionIdLength, LoginConstants.sessionTimeOutMins);
        scoreService = new ScoreService(loginService,LoginConstants.priorityQueueCapacity);
    }


    @GetMapping(value = "{userID}/login")
    public String login(@PathVariable final String userID){

        String login = loginService.doLogin(userID);
        return login;
    }

    /*@PostMapping(value = "/{levelId}/{sessionScore}")
    public void postUserScorePerLevel(@PathVariable(value = "levelId") final String levelId, @RequestParam(value = "sessionId", required = true) String sessionId, @RequestParam(value = "score", required = true) final Integer score){
       scoreService.postScore(levelId, sessionId, score);
    }*/

   //http://localhost:8081/2/score?sessionkey=UICSNDK (with the post body: 1500)
    @PostMapping(value = "/{levelId}/{sessionScore}")
    public void postUserScorePerLevel(@PathVariable(value = "levelId") final String levelId, @RequestParam(value = "sessionScore", required = true) String  session, @RequestBody final Integer score){
        scoreService.postScore(levelId, session, score);
    }

    @GetMapping(value = "/{levelId}/highscorelist")
    public void highScoreList(@PathVariable(value = "levelId") final String levelId){
        scoreService.getHighScoreList(levelId);
    }

}
