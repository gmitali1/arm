package com.arm.coordinator.model;

import com.arm.coordinator.common.Result;

/**
 * Interface for adding new acceptors which extends KeyValueStore.
 *
 * @author mitali ghotgalkar.
 */
public interface CoordinatorInterface {

    /**
     * adds an acceptor with the provided hostname and port.
     *
     * @param hostName of the acceptor
     * @param port     of the acceptor
     */
    void addAcceptor(String hostName, int port);

    Result getAllOrders(int userId);

    Result getAllProducts();

    Result createOrder(OrderForm orderForm);
}

