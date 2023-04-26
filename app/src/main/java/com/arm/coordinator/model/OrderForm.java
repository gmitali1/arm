package com.arm.coordinator.model;

import com.arm.ecommerce.dto.OrderProductDto;

import java.util.List;

public class OrderForm {

    private List<OrderProductDto> productOrders;

    private long orderId;

    private int userId;

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public List<OrderProductDto> getProductOrders() {
        return productOrders;
    }

    public void setProductOrders(List<OrderProductDto> productOrders) {
        this.productOrders = productOrders;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}