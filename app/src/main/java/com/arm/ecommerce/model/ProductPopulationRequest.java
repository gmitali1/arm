package com.arm.ecommerce.model;

/**
 * A request object used to populate products.
 */
public class ProductPopulationRequest {

    /**
     * Iterable of products to be populated.
     */
    private final Iterable<Product> products;

    /**
     * A commit key used to ensure data consistency.
     */
    private final String commitKey;

    /**
     * Constructs a new ProductPopulationRequest with the given products and commit key.
     *
     * @param products  Iterable of products to be populated.
     * @param commitKey A commit key used to ensure data consistency.
     */
    public ProductPopulationRequest(Iterable<Product> products, String commitKey) {
        this.products = products;
        this.commitKey = commitKey;
    }

    /**
     * Returns the iterable of products.
     *
     * @return the iterable of products.
     */
    public Iterable<Product> getProducts() {
        return products;
    }

    /**
     * Returns the commit key.
     *
     * @return the commit key.
     */
    public String getCommitKey() {
        return commitKey;
    }
}