package com.arm.coordinator.controller;

import com.arm.coordinator.model.EcommerceUser;
import com.arm.coordinator.service.UserService;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * This controller handles user-related endpoints for an E-commerce application.
 */

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    /**
     * Creates a new instance of the UserController class.
     *
     * @param userService The UserService object that provides the functionality for handling user operations.
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Handles the user login endpoint.
     *
     * @param username The username of the user attempting to login.
     * @param password The password of the user attempting to login.
     * @return A ResponseEntity containing the EcommerceUser object if the login is successful, or null otherwise.
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public @NotNull ResponseEntity<EcommerceUser> login(@RequestParam String username, @RequestParam String password) {
        EcommerceUser user = this.userService.getByUsernameAndPassword(username, password);
        if (user != null) {
            return new ResponseEntity<>(user, null, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles the user signup endpoint.
     *
     * @param ecommerceUser The EcommerceUser object containing the details of the user to be signed up.
     * @return A ResponseEntity containing the EcommerceUser object if the signup is successful, or null otherwise.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<EcommerceUser> signup(@RequestBody EcommerceUser ecommerceUser) {
        EcommerceUser user = this.userService.getByUsernameAndPassword(ecommerceUser.getUsername(), ecommerceUser.getPassword());
        if (user != null) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(this.userService.signup(ecommerceUser), HttpStatus.CREATED);
    }
}
