package com.example.gonggong_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class GonggongServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GonggongServerApplication.class, args);
    }

}
