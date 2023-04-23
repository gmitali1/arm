package com.arm.coordinator.common;

import com.arm.ecommerce.model.Order;
import com.arm.ecommerce.model.Product;

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

    private Iterable<Order> orders;

    private Iterable<Product> products;

    public Result() {}

    public Result(Boolean ok, String message, ResultCodeEnum resultCodeEnum, Iterable<Order> orders,
                  Iterable<Product> products) {
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

    public Iterable<Order> getOrders() {
        return orders;
    }

    public void setOrders(Iterable<Order> orders) {
        this.orders = orders;
    }

    public Iterable<Product> getProducts() {
        return products;
    }

    public void setProducts(Iterable<Product> products) {
        this.products = products;
    }

}
