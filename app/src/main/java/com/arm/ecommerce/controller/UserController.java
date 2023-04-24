package com.arm.ecommerce.controller;

import com.arm.ecommerce.model.EcommerceUser;
import com.arm.ecommerce.service.UserService;
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
        for(EcommerceUser u :this.userService.getAllUsers()) {
            if (u.getPassword().equals(password) && u.getUsername().equals(username)) {
                return new ResponseEntity<>(u, null, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<EcommerceUser> signup(@RequestBody EcommerceUser ecommerceUser) {
        return new ResponseEntity<>(this.userService.signup(ecommerceUser), null, HttpStatus.CREATED);
    }
}
