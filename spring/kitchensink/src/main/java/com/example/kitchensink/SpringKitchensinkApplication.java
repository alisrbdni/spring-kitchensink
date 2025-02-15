package com.example.kitchensink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class SpringKitchensinkApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringKitchensinkApplication.class, args);
    }
}
