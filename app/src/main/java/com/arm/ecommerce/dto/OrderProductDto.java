package com.arm.ecommerce.dto;

import com.arm.ecommerce.model.Product;

/**
 * A DTO (Data Transfer Object) representing an order of a product.
 */
public class OrderProductDto {

    private Product product;
    private Integer quantity;

    /**
     * Gets the product of the order.
     *
     * @return the product of the order
     */
    public Product getProduct() {
        return product;
    }

    /**
     * Sets the product of the order.
     *
     * @param product the product of the order
     */
    public void setProduct(Product product) {
        this.product = product;
    }

    /**
     * Gets the quantity of the product in the order.
     *
     * @return the quantity of the product in the order
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of the product in the order.
     *
     * @param quantity the quantity of the product in the order
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
