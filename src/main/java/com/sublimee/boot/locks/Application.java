package com.sublimee.boot.locks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.sublimee.boot.locks.repository")
public class Application extends SpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
