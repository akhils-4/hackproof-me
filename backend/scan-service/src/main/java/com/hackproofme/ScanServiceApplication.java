package com.hackproofme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ScanServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScanServiceApplication.class, args);
    }

}
