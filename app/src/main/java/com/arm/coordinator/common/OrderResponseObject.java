package com.arm.coordinator.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.jsonschema.JsonSerializableSchema;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@JsonSerializableSchema
public class OrderResponseObject implements Serializable {

    private Long id;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateCreated;

    private OrderStatus status;

    private List<OrderProductResponseObject> orderProducts;

    private Double totalOrderPrice;

    private Integer numberOfProducts;

    private Integer userId;

    public OrderResponseObject() {
    }

    /**
     * Used to create an OrderResponseObject instance
     *
     * @param id               the order ID
     * @param dateCreated      the date the order was created
     * @param status           the order status
     * @param orderProducts    the list of order products
     * @param totalOrderPrice  the total price of the order
     * @param numberOfProducts the number of products in the order
     */
    public OrderResponseObject(Long id, LocalDate dateCreated, OrderStatus status,
                               List<OrderProductResponseObject> orderProducts, Double totalOrderPrice,
                               Integer numberOfProducts, Integer userId) {
        this.id = id;
        this.dateCreated = dateCreated;
        this.status = status;
        this.orderProducts = orderProducts;
        this.totalOrderPrice = totalOrderPrice;
        this.numberOfProducts = numberOfProducts;
        this.userId = userId;
    }

    /**
     * Gets the order ID.
     *
     * @return the order ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the order ID.
     *
     * @param id the order ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the date the order was created.
     *
     * @return the date the order was created
     */
    public LocalDate getDateCreated() {
        return dateCreated;
    }

    /**
     * Sets the date the order was created.
     *
     * @param dateCreated the date the order was created
     */
    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    /**
     * Gets the order status.
     *
     * @return the order status
     */
    public OrderStatus getStatus() {
        return status;
    }

    /**
     * Sets the order status.
     *
     * @param status the order status
     */
    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    /**
     * Gets the list of order products.
     *
     * @return the list of order products
     */
    public List<OrderProductResponseObject> getOrderProducts() {
        return orderProducts;
    }

    /**
     * Sets the list of order products.
     *
     * @param orderProducts the list of order products
     */
    public void setOrderProducts(List<OrderProductResponseObject> orderProducts) {
        this.orderProducts = orderProducts;
    }

    /**
     * Gets the total price of the order.
     *
     * @return the total price of the order
     */
    public Double getTotalOrderPrice() {
        return totalOrderPrice;
    }

    /**
     * Sets the total price of the order.
     *
     * @param totalOrderPrice the total price of the order
     */
    public void setTotalOrderPrice(Double totalOrderPrice) {
        this.totalOrderPrice = totalOrderPrice;
    }

    /**
     * Gets the number of products in the order.
     *
     * @return the number of products in the order
     */
    public Integer getNumberOfProducts() {
        return numberOfProducts;
    }

    public void setNumberOfProducts(Integer numberOfProducts) {
        this.numberOfProducts = numberOfProducts;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderResponseObject that = (OrderResponseObject) o;
        return Objects.equals(id, that.id) && Objects.equals(dateCreated, that.dateCreated) && Objects.equals(totalOrderPrice, that.totalOrderPrice) && Objects.equals(numberOfProducts, that.numberOfProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateCreated, totalOrderPrice, numberOfProducts);
    }
}
