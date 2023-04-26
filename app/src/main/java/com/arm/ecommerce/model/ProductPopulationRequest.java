package com.arm.ecommerce.model;

public class ProductPopulationRequest {

    private final Iterable<Product> products;

    private final String commitKey;

    public ProductPopulationRequest(Iterable<Product> products, String commitKey) {
        this.products = products;
        this.commitKey = commitKey;
    }

    public Iterable<Product> getProducts() {
        return products;
    }

    public String getCommitKey() {
        return commitKey;
    }
}
