package com.arm.coordinator.common;

import com.arm.coordinator.model.OrderForm;

/**
 * Operation that is to be carried out on the store (key-value store) and the key and value of the store.
 *
 * @author mitali ghotgalkar
 */
public class EcommerceOperation {

    private OperationType operationType;
    private OrderForm orderForm;

    public EcommerceOperation() {

    }

    /**
     * Constructor for creating Key Value Operation
     *
     * @param operationType the type of operation (put,get or delete)

     */
    public EcommerceOperation(OperationType operationType, OrderForm orderForm) {
        this.operationType = operationType;
        this.orderForm = orderForm;
    }

    public OrderForm getOrderForm() {
        return orderForm;
    }

    /**
     * type of operation in enum
     *
     * @return operationType
     */
    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public void setOrderForm(OrderForm orderForm) {
        this.orderForm = orderForm;
    }
}
