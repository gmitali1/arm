package com.arm.coordinator.common;

public class ProductPopulationRequest {

    private final Iterable<ProductResponseObject> products;

    private final String commitKey;

    public ProductPopulationRequest(Iterable<ProductResponseObject> products, String commitKey) {
        this.products = products;
        this.commitKey = commitKey;
    }

    public Iterable<ProductResponseObject> getProducts() {
        return products;
    }

    public String getCommitKey() {
        return commitKey;
    }
}
