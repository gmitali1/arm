package com.arm.coordinator.common;

import com.fasterxml.jackson.databind.jsonschema.JsonSerializableSchema;

import java.util.List;

/**
 * This class represents a wrapper object that contains a list of {@link OrderResponseObject} objects.
 */
@JsonSerializableSchema
public class OrderResponseWrapperObject {

    private List<OrderResponseObject> orders;

    /**
     * Default constructor for creating an OrderResponseWrapperObject instance.
     */
    public OrderResponseWrapperObject() {
    }

    /**
     * Constructor for creating an OrderResponseWrapperObject instance with a list of orders.
     *
     * @param orders the list of orders to be wrapped
     */
    public OrderResponseWrapperObject(List<OrderResponseObject> orders) {
        this.orders = orders;
    }

    /**
     * Gets the list of orders.
     *
     * @return the list of orders
     */
    public Iterable<OrderResponseObject> getOrders() {
        return orders;
    }

    /**
     * Sets the list of orders.
     *
     * @param orders the list of orders to be set
     */
    public void setOrders(List<OrderResponseObject> orders) {
        this.orders = orders;
    }
}
