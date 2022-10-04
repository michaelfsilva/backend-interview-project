package com.ninjaone.backendinterviewproject.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan("com.ninjaone.backendinterviewproject.*")
@EnableJpaRepositories("com.ninjaone.backendinterviewproject.resources")
@EntityScan(basePackages = {"com.ninjaone.backendinterviewproject.resources"})
public class BackendInterviewProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendInterviewProjectApplication.class, args);
    }
}
