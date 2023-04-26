package com.arm.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;

import java.util.Objects;

/**
 * Entity representing an order-product relationship in the ecommerce application.
 */
@Entity
public class OrderProduct {

    /**
     * The primary key for the order-product relationship.
     */
    @EmbeddedId
    @JsonIgnore
    private OrderProductPK pk;

    /**
     * The quantity of the product in the order.
     */
    @Column(nullable = false)
    private Integer quantity;

    /**
     * Constructs a new OrderProduct object.
     *
     * @param order    the order object for the relationship
     * @param product  the product object for the relationship
     * @param quantity the quantity of the product in the order
     */
    public OrderProduct(Order order, Product product, Integer quantity) {
        pk = new OrderProductPK();
        pk.setOrder(order);
        pk.setProduct(product);
        this.quantity = quantity;
    }

    /**
     * Default constructor required by JPA.
     */
    public OrderProduct() {

    }

    /**
     * Returns the product associated with the order-product relationship.
     *
     * @return the product object
     */
    @Transient
    public Product getProduct() {
        return this.pk.getProduct();
    }

    /**
     * Returns the total price of the order-product relationship.
     *
     * @return the total price
     */
    @Transient
    public Double getTotalPrice() {
        return getProduct().getPrice() * getQuantity();
    }

    /**
     * Returns the primary key of the order-product relationship.
     *
     * @return the primary key object
     */
    public OrderProductPK getPk() {
        return pk;
    }

    /**
     * Sets the primary key of the order-product relationship.
     *
     * @param pk the primary key object
     */
    public void setPk(OrderProductPK pk) {
        this.pk = pk;
    }

    /**
     * Returns the quantity of the product in the order.
     *
     * @return the quantity
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of the product in the order.
     *
     * @param quantity the quantity
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderProduct that = (OrderProduct) o;
        return Objects.equals(pk, that.pk) && Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pk, quantity);
    }
}
