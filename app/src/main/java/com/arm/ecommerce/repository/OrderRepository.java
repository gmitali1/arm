package com.arm.ecommerce.repository;

import com.arm.ecommerce.model.Order;
import org.springframework.data.repository.CrudRepository;

/**
 * Provides CRUD operations for {@link Order} entity.
 */
public interface OrderRepository extends CrudRepository<Order, Long> {
}
