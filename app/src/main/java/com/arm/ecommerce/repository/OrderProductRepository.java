package com.arm.ecommerce.repository;

import com.arm.ecommerce.model.OrderProduct;
import com.arm.ecommerce.model.OrderProductPK;
import org.springframework.data.repository.CrudRepository;

/**
 * Provides CRUD operations for {@link OrderProduct} entity.
 */
public interface OrderProductRepository extends CrudRepository<OrderProduct, OrderProductPK> {
}
