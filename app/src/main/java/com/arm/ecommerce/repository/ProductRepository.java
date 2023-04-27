package com.arm.ecommerce.repository;

import com.arm.ecommerce.model.Product;
import org.springframework.data.repository.CrudRepository;

/**
 * Provides CRUD operations for {@link Product} entity.
 */
public interface ProductRepository extends CrudRepository<Product, Long> {
}
