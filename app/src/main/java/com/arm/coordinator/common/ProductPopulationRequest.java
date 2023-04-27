package com.arm.coordinator.common;

/**
 * A class representing a request to populate products.
 */
public class ProductPopulationRequest {

    private final Iterable<ProductResponseObject> products;

    private final String commitKey;

    /**
     * Creates a new instance of ProductPopulationRequest.
     *
     * @param products  an iterable collection of ProductResponseObject to be populated.
     * @param commitKey a string representing the commit key.
     */
    public ProductPopulationRequest(Iterable<ProductResponseObject> products, String commitKey) {
        this.products = products;
        this.commitKey = commitKey;
    }

    /**
     * Gets the iterable collection of products to be populated.
     *
     * @return an iterable collection of ProductResponseObject.
     */
    public Iterable<ProductResponseObject> getProducts() {
        return products;
    }

    /**
     * Gets the commit key.
     *
     * @return a string representing the commit key.
     */
    public String getCommitKey() {
        return commitKey;
    }
}
