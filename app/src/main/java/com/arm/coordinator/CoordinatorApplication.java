package com.arm.coordinator;

import com.arm.coordinator.model.EcommerceUser;
import com.arm.coordinator.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CoordinatorApplication {
    public static void main(String[] args) {
        SpringApplication.run(CoordinatorApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(UserService userService) {
        return args -> {
            userService.signup(new EcommerceUser(1L, "abc", "abc"));
            userService.signup(new EcommerceUser(2L, "xyz", "uvw"));
        };
    }

}
