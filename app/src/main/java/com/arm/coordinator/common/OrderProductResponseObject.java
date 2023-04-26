package com.arm.coordinator.common;

public class OrderProductResponseObject {

    private int quantity;

    private ProductResponseObject product;

    public OrderProductResponseObject() {
    }

    public OrderProductResponseObject(int quantity, ProductResponseObject product) {
        this.quantity = quantity;
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public ProductResponseObject getProduct() {
        return product;
    }

    public void setProduct(ProductResponseObject product) {
        this.product = product;
    }
}
