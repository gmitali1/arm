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