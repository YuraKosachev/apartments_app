package com.apartment.apartment_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ApartmentApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApartmentApiApplication.class, args);
    }

}
