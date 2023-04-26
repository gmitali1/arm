package com.arm.ecommerce.service;

import com.arm.ecommerce.model.Order;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

/**
 * The interface for OrderService, which provides methods for creating, updating and managing orders.
 * This interface is validated using the @Validated annotation.
 */
@Validated
public interface OrderService {

    /**
     * Gets all orders.
     *
     * @return An iterable collection of orders.
     */
    @NotNull Iterable<Order> getAllOrders();

    /**
     * Creates a new order and validates that it is not null or empty.
     *
     * @param order The order to create.
     * @return The created order.
     */
    Order create(@NotNull(message = "The order cannot be null.") @Valid Order order);

    /**
     * Updates an existing order and validates that it is not null or empty.
     *
     * @param order The order to update.
     */
    void update(@NotNull(message = "The order cannot be null.") @Valid Order order);

    /**
     * Gets all orders for all users.
     *
     * @return An iterable collection of orders for all users.
     */
    @NotNull Iterable<Order> getAllOrdersOfAllUsers();

}
