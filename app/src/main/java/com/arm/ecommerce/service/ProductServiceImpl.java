package com.arm.ecommerce.service;

import com.arm.ecommerce.exception.ResourceNotFoundException;
import com.arm.ecommerce.model.Product;
import com.arm.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the {@link ProductService} interface that provides CRUD operations
 * for {@link Product} entities using a {@link ProductRepository}.
 */
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    /**
     * Constructs a new {@code ProductServiceImpl} instance with the given {@link ProductRepository}.
     *
     * @param productRepository the {@link ProductRepository} used to access the product data.
     */
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Returns all products stored in the database.
     *
     * @return an {@link Iterable} of {@link Product} entities.
     */
    @Override
    public Iterable<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Retrieves a product with the given ID from the database.
     *
     * @param id the ID of the product to retrieve.
     * @return the {@link Product} entity with the given ID.
     * @throws ResourceNotFoundException if no product is found with the given ID.
     */
    @Override
    public Product getProduct(long id) {
        return productRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    /**
     * Saves the given products to the database.
     *
     * @param products an {@link Iterable} of {@link Product} entities to save.
     * @return {@code true} if the products were saved successfully, {@code false} otherwise.
     */
    @Override
    public boolean saveAll(Iterable<Product> products) {
        try {
            productRepository.saveAll(products);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
