package com.arm.ecommerce.service;

import com.arm.ecommerce.model.Product;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

/**
 * Interface for managing products.
 * Includes methods to retrieve all products, retrieve a single product by ID, and save a list of products.
 */
@Validated
public interface ProductService {

    /**
     * Retrieves all products.
     *
     * @return an iterable of all products
     */
    @NotNull Iterable<Product> getAllProducts();

    /**
     * Retrieves a single product by ID.
     *
     * @param id the ID of the product to retrieve
     * @return the product with the specified ID
     */
    Product getProduct(@Min(value = 1L, message = "Invalid product ID.") long id);

    /**
     * Saves a list of products.
     *
     * @param products the products to save
     * @return true if the save was successful, false otherwise
     */
    boolean saveAll(Iterable<Product> products);

}
