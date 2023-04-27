package com.arm.coordinator.common;

/**
 * This class represents the response object for a product included in an order.
 * It includes the quantity of the product and the corresponding {@link ProductResponseObject}.
 */
public class OrderProductResponseObject {

    /**
     * The quantity of the product included in the order.
     */
    private int quantity;

    /**
     * The {@link ProductResponseObject} that represents the product included in the order.
     */
    private ProductResponseObject product;

    /**
     * Creates a new instance of {@code OrderProductResponseObject} with the specified quantity and product.
     *
     * @param quantity the quantity of the product included in the order.
     * @param product  the {@link ProductResponseObject} that represents the product included in the order.
     */
    public OrderProductResponseObject(int quantity, ProductResponseObject product) {
        this.quantity = quantity;
        this.product = product;
    }

    /**
     * Gets the quantity of the product included in the order.
     *
     * @return the quantity of the product included in the order.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of the product included in the order.
     *
     * @param quantity the quantity of the product included in the order.
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Gets the {@link ProductResponseObject} that represents the product included in the order.
     *
     * @return the {@link ProductResponseObject} that represents the product included in the order.
     */
    public ProductResponseObject getProduct() {
        return product;
    }

    /**
     * Sets the {@link ProductResponseObject} that represents the product included in the order.
     *
     * @param product the {@link ProductResponseObject} that represents the product included in the order.
     */
    public void setProduct(ProductResponseObject product) {
        this.product = product;
    }
}
