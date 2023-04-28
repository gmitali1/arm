package com.arm.coordinator.model;

import com.arm.coordinator.common.Result;

/**
 * This interface defines the methods that a Coordinator implementation must provide.
 */
public interface CoordinatorInterface {

    /**
     * Adds an acceptor with the given host name and port to the coordinator.
     *
     * @param hostName the host name of the acceptor
     * @param port     the port of the acceptor
     */
    void addAcceptor(String hostName, int port);

    /**
     * Returns a Result object containing all orders.
     *
     * @return a Result object containing all orders
     */
    Result getAllOrders(Integer userId);

    /**
     * Returns a Result object containing all products.
     *
     * @return a Result object containing all products
     */
    Result getAllProducts(Integer userId);

    /**
     * Creates an order using the given OrderForm.
     *
     * @param orderForm the OrderForm used to create the order
     * @param userId    User Id for the order
     * @return a Result object indicating whether the order creation was successful
     */
    Result createOrder(OrderForm orderForm, Integer userId);
}

