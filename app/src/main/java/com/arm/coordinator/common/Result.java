package com.arm.coordinator.common;

import java.io.Serializable;

/**
 * The Result class represents the result of a put/get/delete operation on a key value store in a distributed system.
 */
public class Result implements Serializable {

    /**
     * A boolean indicating whether the operation was successful or not.
     */
    private Boolean ok;

    /**
     * A message associated with the operation result.
     */
    private String message;

    /**
     * The ResultCodeEnum associated with the operation result.
     */
    private ResultCodeEnum resultCodeEnum;

    /**
     * An Iterable of OrderResponseObject instances associated with the operation result.
     */
    private Iterable<OrderResponseObject> orders;

    /**
     * An Iterable of ProductResponseObject instances associated with the operation result.
     */
    private Iterable<ProductResponseObject> products;

    /**
     * Constructs a new instance of the Result class with default values.
     */
    public Result() {
    }

    /**
     * Constructs a new instance of the Result class with the specified values.
     *
     * @param ok             The boolean indicating whether the operation was successful or not.
     * @param message        The message associated with the operation result.
     * @param resultCodeEnum The ResultCodeEnum associated with the operation result.
     * @param orders         An Iterable of OrderResponseObject instances associated with the operation result.
     * @param products       An Iterable of ProductResponseObject instances associated with the operation result.
     */
    public Result(Boolean ok, String message, ResultCodeEnum resultCodeEnum, Iterable<OrderResponseObject> orders,
                  Iterable<ProductResponseObject> products) {
        this.ok = ok;
        this.message = message;
        this.resultCodeEnum = resultCodeEnum;
        this.orders = orders;
        this.products = products;
    }

    /**
     * Returns a boolean indicating whether the operation was successful or not.
     *
     * @return A boolean indicating whether the operation was successful or not.
     */
    public Boolean isOk() {
        return ok;
    }

    /**
     * Sets the boolean indicating whether the operation was successful or not.
     *
     * @param ok The boolean indicating whether the operation was successful or not.
     */
    public void setOk(Boolean ok) {
        this.ok = ok;
    }

    /**
     * Returns the message associated with the operation result.
     *
     * @return The message associated with the operation result.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message associated with the operation result.
     *
     * @param message The message associated with the operation result.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Returns the ResultCodeEnum associated with the operation result.
     *
     * @return The ResultCodeEnum associated with the operation result.
     */
    public ResultCodeEnum getResultCodeEnum() {
        return resultCodeEnum;
    }

    /**
     * Sets the ResultCodeEnum associated with the operation result.
     *
     * @param resultCodeEnum The ResultCodeEnum associated with the operation result.
     */
    public void setResultCodeEnum(ResultCodeEnum resultCodeEnum) {
        this.resultCodeEnum = resultCodeEnum;
    }

    /**
     * Returns an Iterable of OrderResponseObject instances associated with the operation result.
     *
     * @return An Iterable of OrderResponseObject instances associated with the operation result.
     */
    public Iterable<OrderResponseObject> getOrders() {
        return orders;
    }

    /**
     * Sets the Iterable of OrderResponseObject instances associated with the operation result.
     *
     * @param orders An Iterable of OrderResponseObject instances associated with the operation result.
     */
    public void setOrders(Iterable<OrderResponseObject> orders) {
        this.orders = orders;
    }

    /**
     * Returns an Iterable of ProductResponseObject instances associated with the operation result.
     *
     * @return An Iterable of ProductResponseObject instances associated with the operation result.
     */
    public Iterable<ProductResponseObject> getProducts() {
        return products;
    }

    /**
     * Sets the Iterable of ProductResponseObject instances associated with the operation result.
     *
     * @param products An Iterable of ProductResponseObject instances associated with the operation result.
     */
    public void setProducts(Iterable<ProductResponseObject> products) {
        this.products = products;
    }

}
