package com.arm.coordinator.service;

import com.arm.coordinator.model.EcommerceUser;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

/**
 * The UserService interface provides methods to access and manage ecommerce users.
 * It is annotated with @Validated to enable validation of method arguments.
 */
@Validated
public interface UserService {

    /**
     * Returns all ecommerce users.
     *
     * @return an iterable collection of ecommerce users
     */
    EcommerceUser getByUsernameAndPassword(String username, String password);

    /**
     * Creates a new ecommerce user.
     *
     * @param ecommerceUser the ecommerce user to be created
     * @return the newly created ecommerce user
     */
    EcommerceUser signup(@Valid EcommerceUser ecommerceUser);
}