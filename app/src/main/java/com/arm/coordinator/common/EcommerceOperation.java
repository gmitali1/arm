package com.arm.coordinator.common;

import com.arm.coordinator.model.OrderForm;

/**
 * Operation that is to be carried out on the store (key-value store) and the key and value of the store.
 *
 * @author mitali ghotgalkar
 */
/**
 * A class representing an ecommerce operation, which can be a key-value operation (put, get or delete) with an associated order form.
 */
public class EcommerceOperation {

    /**
     * The type of operation (put, get or delete).
     */
    private OperationType operationType;

    /**
     * The order form associated with the operation.
     */
    private OrderForm orderForm;

    /**
     * Default constructor for EcommerceOperation class.
     */
    public EcommerceOperation() {

    }

    /**
     * Constructor for creating a key-value operation.
     *
     * @param operationType The type of operation (put, get or delete).
     * @param orderForm     The order form associated with the operation.
     */
    public EcommerceOperation(OperationType operationType, OrderForm orderForm) {
        this.operationType = operationType;
        this.orderForm = orderForm;
    }

    /**
     * Gets the order form associated with the operation.
     *
     * @return The order form associated with the operation.
     */
    public OrderForm getOrderForm() {
        return orderForm;
    }

    /**
     * Sets the order form associated with the operation.
     *
     * @param orderForm The order form to set.
     */
    public void setOrderForm(OrderForm orderForm) {
        this.orderForm = orderForm;
    }

    /**
     * Gets the type of operation (put, get or delete).
     *
     * @return The type of operation.
     */
    public OperationType getOperationType() {
        return operationType;
    }

    /**
     * Sets the type of operation (put, get or delete).
     *
     * @param operationType The type of operation to set.
     */
    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }
}
