package com.arm.coordinator.service;

import com.arm.coordinator.model.EcommerceUser;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public interface UserService {
    @NotNull Iterable<EcommerceUser> getAllUsers();

    EcommerceUser signIn(String username, String password);

    EcommerceUser signup(@Valid EcommerceUser ecommerceUser);
}
