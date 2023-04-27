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
            userService.signup(new EcommerceUser(1L, "anuj", "potdar"));
            userService.signup(new EcommerceUser(2L, "rush", "mokashi"));
            userService.signup(new EcommerceUser(3L, "mitali", "ghotgalkar"));
            userService.signup(new EcommerceUser(4L, "rudra", "mishra"));
            userService.signup(new EcommerceUser(5L, "nayana", "hugar"));
            userService.signup(new EcommerceUser(6L, "srikar", "kokkanti"));
            userService.signup(new EcommerceUser(7L, "aniruddha", "achar"));
            userService.signup(new EcommerceUser(8L, "gourav", "beura"));
            userService.signup(new EcommerceUser(9L, "pavan", "sai"));
            userService.signup(new EcommerceUser(10L, "malhar", "mahant"));
        };
    }

}
