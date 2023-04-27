package com.arm.ecommerce.service;

import com.arm.ecommerce.model.OrderProduct;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;


/**
 * The interface for OrderProductService, which provides methods for creating and managing order products.
 * This interface is validated using the @Validated annotation.
 */
@Validated
public interface OrderProductService {

    /**
     * Creates a new OrderProduct and validates that it is not null or empty.
     *
     * @param orderProduct The OrderProduct to create.
     * @return The created OrderProduct.
     */
    OrderProduct create(@NotNull(message = "The products for order cannot be null.") @Valid OrderProduct orderProduct);
}


