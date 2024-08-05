package com.example.eurekaback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;


@SpringBootApplication
@EnableEurekaServer

public class EurekaBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaBackApplication.class, args);
    }

}
