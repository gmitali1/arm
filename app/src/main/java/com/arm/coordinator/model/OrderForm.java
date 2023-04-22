package com.arm.coordinator.model;

import com.arm.ecommerce.dto.OrderProductDto;

import java.util.List;

public class OrderForm {

    private List<OrderProductDto> productOrders;

    public List<OrderProductDto> getProductOrders() {
        return productOrders;
    }

    public void setProductOrders(List<OrderProductDto> productOrders) {
        this.productOrders = productOrders;
    }
}