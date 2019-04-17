package ru.mera.readmeCreator.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Main starting class of web service.
 * It delegates start of the service to SpringBoot.
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    RTFGenerator initRTFGenerator() {
        return new RTFGenerator();
    }

    @Bean
    FileRepo initFileRepo() {
        return new FileRepo(initRTFGenerator());
    }
}