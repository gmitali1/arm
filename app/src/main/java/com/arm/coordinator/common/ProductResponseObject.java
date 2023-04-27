package com.arm.coordinator.common;

import com.fasterxml.jackson.databind.jsonschema.JsonSerializableSchema;

/**
 * Class representing a response object for a product in the ecommerce system.
 * Includes the product's ID, name, price, and picture URL.
 * This class is annotated with the {@code @JsonSerializableSchema} annotation.
 */
@JsonSerializableSchema
public class ProductResponseObject {

    private Long id;

    /**
     * The name of the product.
     */
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
     * Constructs a new {@code ProductResponseObject} with the given ID, name, price, and picture URL.
     *
     * @param id         the ID of the product
     * @param name       the name of the product
     * @param price      the price of the product
     * @param pictureUrl the URL of the product's picture
     */
    public ProductResponseObject(Long id, String name, Double price, String pictureUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.pictureUrl = pictureUrl;
    }

    /**
     * Default constructor for the {@code ProductResponseObject} class.
     */
    public ProductResponseObject() {
    }

    /**
     * Returns the ID of the product.
     *
     * @return the ID of the product
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the ID of the product.
     *
     * @param id the ID to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns the name of the product.
     *
     * @return the name of the product
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the product.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the price of the product.
     *
     * @return the price of the product
     */
    public Double getPrice() {
        return price;
    }

    /**
     * Sets the price of the product.
     *
     * @param price the price to set
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * Returns the URL of the product's picture.
     *
     * @return the URL of the product's picture
     */
    public String getPictureUrl() {
        return pictureUrl;
    }

    /**
     * Sets the URL of the product's picture.
     *
     * @param pictureUrl the URL to set
     */
    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
