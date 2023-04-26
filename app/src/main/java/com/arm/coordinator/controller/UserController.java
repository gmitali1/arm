package com.arm.coordinator.controller;

import com.arm.coordinator.model.EcommerceUser;
import com.arm.coordinator.service.UserService;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public @NotNull ResponseEntity<EcommerceUser> login(@RequestParam String username, @RequestParam String password) {
        System.out.println(username + "-" + password);
        EcommerceUser ecommerceUser = userService.signIn(username, password);
        if (ecommerceUser != null) {
            return new ResponseEntity<>(ecommerceUser, HttpStatus.OK);
        }

        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<EcommerceUser> signup(@RequestBody EcommerceUser ecommerceUser) {
        for(EcommerceUser u :this.userService.getAllUsers()) {
            if (u.getUsername().equals(ecommerceUser.getUsername())) {
                return new ResponseEntity<>(null, null, HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<>(this.userService.signup(ecommerceUser), HttpStatus.CREATED);
    }
}
