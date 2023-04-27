package com.arm.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.io.Serializable;
import java.util.Objects;

/**
 * Embeddable class representing the primary key of the OrderProduct entity.
 */
@Embeddable
public class OrderProductPK implements Serializable {

    @JsonBackReference
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    /**
     * Get the order associated with this OrderProductPK.
     *
     * @return The order associated with this OrderProductPK.
     */
    public Order getOrder() {
        return order;
    }

    /**
     * Set the order associated with this OrderProductPK.
     *
     * @param order The order to associate with this OrderProductPK.
     */
    public void setOrder(Order order) {
        this.order = order;
    }

    /**
     * Get the product associated with this OrderProductPK.
     *
     * @return The product associated with this OrderProductPK.
     */
    public Product getProduct() {
        return product;
    }

    /**
     * Set the product associated with this OrderProductPK.
     *
     * @param product The product to associate with this OrderProductPK.
     */
    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderProductPK that = (OrderProductPK) o;
        return Objects.equals(order, that.order) && Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(order, product);
    }
}
