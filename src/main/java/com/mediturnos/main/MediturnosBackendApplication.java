package com.mediturnos.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MediturnosBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MediturnosBackendApplication.class, args);
    }

}
