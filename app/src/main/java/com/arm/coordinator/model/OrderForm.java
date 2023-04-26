package com.arm.coordinator.model;

import com.arm.ecommerce.dto.OrderProductDto;

import java.util.List;

public class OrderForm {

    private List<OrderProductDto> productOrders;

    private Long orderId;

    private Integer userId;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public List<OrderProductDto> getProductOrders() {
        return productOrders;
    }

    public void setProductOrders(List<OrderProductDto> productOrders) {
        this.productOrders = productOrders;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}