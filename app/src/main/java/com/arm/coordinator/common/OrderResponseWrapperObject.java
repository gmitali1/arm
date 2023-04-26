package com.arm.coordinator.common;

import com.fasterxml.jackson.databind.jsonschema.JsonSerializableSchema;

import java.util.List;

@JsonSerializableSchema
public class OrderResponseWrapperObject {

    private List<OrderResponseObject> orders;

    public OrderResponseWrapperObject() {

    }

    public OrderResponseWrapperObject(List<OrderResponseObject> orders) {
        this.orders = orders;
    }

    public Iterable<OrderResponseObject> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderResponseObject> orders) {
        this.orders = orders;
    }
}
