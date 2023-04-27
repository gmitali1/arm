package com.arm.ecommerce.model;

/**
 * A wrapper class for an iterable collection of orders.
 */
public class OrderWrapper {

    private Iterable<Order> orders;

    /**
     * Constructs an empty order wrapper.
     */
    public OrderWrapper() {

    }

    /**
     * Constructs an order wrapper with the given orders.
     *
     * @param orders the orders to wrap
     */
    public OrderWrapper(Iterable<Order> orders) {
        this.orders = orders;
    }

    /**
     * Returns the orders in this wrapper.
     *
     * @return the orders in this wrapper
     */
    public Iterable<Order> getOrders() {
        return orders;
    }

    /**
     * Sets the orders in this wrapper.
     *
     * @param orders the orders to set
     */
    public void setOrders(Iterable<Order> orders) {
        this.orders = orders;
    }
}
