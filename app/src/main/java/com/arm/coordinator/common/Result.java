package com.arm.coordinator.common;

import java.io.Serializable;

/**
 * Enum that stores the result after execution of put/get/delete on the key value store.
 *
 * @author mitali ghotgalkar
 */
public class Result implements Serializable {

    private Boolean ok;
    private String message;
    private ResultCodeEnum resultCodeEnum;

    private Iterable<OrderResponseObject> orders;

    private Iterable<ProductResponseObject> products;

    public Result() {
    }

    public Result(Boolean ok, String message, ResultCodeEnum resultCodeEnum, Iterable<OrderResponseObject> orders,
                  Iterable<ProductResponseObject> products) {
        this.ok = ok;
        this.message = message;
        this.resultCodeEnum = resultCodeEnum;
        this.orders = orders;
        this.products = products;
    }

    public Boolean isOk() {
        return ok;
    }

    public void setOk(Boolean ok) {
        this.ok = ok;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResultCodeEnum getResultCodeEnum() {
        return resultCodeEnum;
    }

    public void setResultCodeEnum(ResultCodeEnum resultCodeEnum) {
        this.resultCodeEnum = resultCodeEnum;
    }

    public Iterable<OrderResponseObject> getOrders() {
        return orders;
    }

    public void setOrders(Iterable<OrderResponseObject> orders) {
        this.orders = orders;
    }

    public Iterable<ProductResponseObject> getProducts() {
        return products;
    }

    public void setProducts(Iterable<ProductResponseObject> products) {
        this.products = products;
    }

}
