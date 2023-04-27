package com.arm.ecommerce.service;

import com.arm.ecommerce.model.EcommerceUser;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
    @NotNull Iterable<EcommerceUser> getAllUsers();

    /**
     * Creates a new ecommerce user.
     *
     * @param ecommerceUser the ecommerce user to be created
     * @return the newly created ecommerce user
     */
    EcommerceUser signup(@Valid EcommerceUser ecommerceUser);
}