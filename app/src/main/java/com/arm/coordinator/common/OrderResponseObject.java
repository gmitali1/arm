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

    public OrderResponseObject() {
    }

    public OrderResponseObject(Long id, LocalDate dateCreated, OrderStatus status, List<OrderProductResponseObject> orderProducts, Double totalOrderPrice, Integer numberOfProducts) {
        this.id = id;
        this.dateCreated = dateCreated;
        this.status = status;
        this.orderProducts = orderProducts;
        this.totalOrderPrice = totalOrderPrice;
        this.numberOfProducts = numberOfProducts;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public List<OrderProductResponseObject> getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(List<OrderProductResponseObject> orderProducts) {
        this.orderProducts = orderProducts;
    }

    public Double getTotalOrderPrice() {
        return totalOrderPrice;
    }

    public void setTotalOrderPrice(Double totalOrderPrice) {
        this.totalOrderPrice = totalOrderPrice;
    }

    public Integer getNumberOfProducts() {
        return numberOfProducts;
    }

    public void setNumberOfProducts(Integer numberOfProducts) {
        this.numberOfProducts = numberOfProducts;
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
