package ru.mera.readmeCreator.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {
    final static Logger logger = LoggerFactory.getLogger(FileController.class);

    public static void main(String[] args) {
        logger.info("Start of the app");

        SpringApplication.run(Application.class, args);
    }

    @Bean
    RTFGenerator initRTFGenerator() {
        return new RTFGenerator();
    }
}
