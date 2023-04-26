package com.arm.coordinator.common;

import com.fasterxml.jackson.databind.jsonschema.JsonSerializableSchema;

@JsonSerializableSchema
public class ProductResponseObject {

    private Long id;

    private String name;

    private Double price;

    private String pictureUrl;

    public ProductResponseObject() {
    }

    public ProductResponseObject(Long id, String name, Double price, String pictureUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.pictureUrl = pictureUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
