package com.arm.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents an Order entity in the e-commerce system.
 */
@Entity
@Table(name = "orders")
public class Order {

    /**
     * The id of the order.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The date the order was created.
     */
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateCreated;

    /**
     * The status of the order.
     */
    private String status;

    /**
     * The list of products in the order.
     */
    @JsonManagedReference
    @OneToMany(mappedBy = "pk.order")
    @Valid
    private List<OrderProduct> orderProducts = new ArrayList<>();

    private Integer userId;

    /**
     * Creates a new instance of the Order class.
     */
    public Order() {

    }

    /**
     * Gets the total price of the order.
     *
     * @return The total price of the order.
     */
    @Transient
    public Double getTotalOrderPrice() {
        double sum = 0D;
        List<OrderProduct> orderProducts = getOrderProducts();
        for (OrderProduct op : orderProducts) {
            sum += op.getTotalPrice();
        }
        return sum;
    }

    /**
     * Gets the number of products in the order.
     *
     * @return The number of products in the order.
     */
    @Transient
    public int getNumberOfProducts() {
        return this.orderProducts.size();
    }

    /**
     * Gets the id of the order.
     *
     * @return The id of the order.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the id of the order.
     *
     * @param id The id of the order.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the date the order was created.
     *
     * @return The date the order was created.
     */
    public LocalDate getDateCreated() {
        return dateCreated;
    }

    /**
     * Sets the date the order was created.
     *
     * @param dateCreated The date the order was created.
     */
    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    /**
     * Gets the status of the order.
     *
     * @return The status of the order.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status of the order.
     *
     * @param status The status of the order.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets the list of products in the order.
     *
     * @return The list of products in the order.
     */
    public List<OrderProduct> getOrderProducts() {
        return orderProducts;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * Sets the list of products in the order.
     *
     * @param orderProducts The list of products in the order.
     */
    public void setOrderProducts(List<OrderProduct> orderProducts) {
        this.orderProducts = orderProducts;
    }

    /**
     * Determines whether this Order object is equal to another object.
     *
     * @param o The object to compare to this Order object.
     * @return True if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) && Objects.equals(dateCreated, order.dateCreated) && Objects.equals(status, order.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateCreated, status);
    }
}
