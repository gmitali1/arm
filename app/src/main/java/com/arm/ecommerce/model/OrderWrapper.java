package com.arm.ecommerce.model;

public class OrderWrapper {

    private Iterable<Order> orders;

    public OrderWrapper() {

    }

    public OrderWrapper(Iterable<Order> orders) {
        this.orders = orders;
    }

    public Iterable<Order> getOrders() {
        return orders;
    }

    public void setOrders(Iterable<Order> orders) {
        this.orders = orders;
    }
}
