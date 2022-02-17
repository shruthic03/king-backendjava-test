package com.king.java.backend.test.javatest;

import com.king.java.backend.test.javatest.controller.KingMiniGameController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;

import static com.king.java.backend.test.javatest.controller.KingMiniGameController.startApp;

@SpringBootApplication
public class KingJavaTestApplication {

	public static void main(String[] args) throws IOException {

		SpringApplication.run(KingJavaTestApplication.class, args);
		startApp();
	}

}
