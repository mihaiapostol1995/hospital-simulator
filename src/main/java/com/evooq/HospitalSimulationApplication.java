package com.evooq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class HospitalSimulationApplication {

    public static void main(String[] args) {
        SpringApplication.run(HospitalSimulationApplication.class, args);
    }
}