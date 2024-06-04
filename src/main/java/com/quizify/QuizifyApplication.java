package com.quizify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class QuizifyApplication {

    public static void main(String[] args) {

        ApplicationContext context =SpringApplication.run(QuizifyApplication.class, args);
    }

}
