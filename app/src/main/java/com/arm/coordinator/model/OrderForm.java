package com.arm.coordinator.model;

import com.arm.ecommerce.dto.OrderProductDto;

import java.util.List;

/**
 * This class represents an order form, which includes a list of product orders and an order ID.
 */
public class OrderForm {

    /**
     * The list of product orders in the order form.
     */
    private List<OrderProductDto> productOrders;

    /**
     * The ID of the order form.
     */
    private long orderId;

    /**
     * Gets the ID of the order form.
     *
     * @return the ID of the order form
     */
    public long getOrderId() {
        return orderId;
    }

    /**
     * Sets the ID of the order form.
     *
     * @param orderId the ID of the order form
     */
    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    /**
     * Gets the list of product orders in the order form.
     *
     * @return the list of product orders in the order form
     */
    public List<OrderProductDto> getProductOrders() {
        return productOrders;
    }

    /**
     * Sets the list of product orders in the order form.
     *
     * @param productOrders the list of product orders in the order form
     */
    public void setProductOrders(List<OrderProductDto> productOrders) {
        this.productOrders = productOrders;
    }

}