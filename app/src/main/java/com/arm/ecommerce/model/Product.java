package com.arm.ecommerce.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

/**
 * Represents a product in the online store.
 */
@Entity
public class Product {

    /**
     * The unique identifier of the product.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the product.
     */
    @NotNull(message = "Product name is required.")
    @Basic(optional = false)
    private String name;

    /**
     * The price of the product.
     */
    private Double price;

    /**
     * The URL of the product's picture.
     */
    private String pictureUrl;

    /**
     * Creates a new instance of the {@code Product} class with the specified identifier, name, price, and picture URL.
     *
     * @param id         The unique identifier of the product.
     * @param name       The name of the product.
     * @param price      The price of the product.
     * @param pictureUrl The URL of the product's picture.
     */
    public Product(Long id, @NotNull(message = "Product name is required.") String name, Double price, String pictureUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.pictureUrl = pictureUrl;
    }

    /**
     * Creates a new instance of the {@code Product} class.
     */
    public Product() {
    }

    /**
     * Gets the unique identifier of the product.
     *
     * @return The unique identifier of the product.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the product.
     *
     * @param id The unique identifier of the product.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the name of the product.
     *
     * @return The name of the product.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the product.
     *
     * @param name The name of the product.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the price of the product.
     *
     * @return The price of the product.
     */
    public Double getPrice() {
        return price;
    }

    /**
     * Sets the price of the product.
     *
     * @param price The price of the product.
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * Gets the URL of the product's picture.
     *
     * @return The URL of the product's picture.
     */
    public String getPictureUrl() {
        return pictureUrl;
    }

    /**
     * Sets the URL of the product's picture.
     *
     * @param pictureUrl The URL of the product's picture.
     */
    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}