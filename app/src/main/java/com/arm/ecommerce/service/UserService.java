package com.arm.ecommerce.service;

import com.arm.ecommerce.model.EcommerceUser;
import com.arm.ecommerce.model.Product;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public interface UserService {
    @NotNull Iterable<EcommerceUser> getAllUsers();

    EcommerceUser signup(@Valid EcommerceUser ecommerceUser);
}
