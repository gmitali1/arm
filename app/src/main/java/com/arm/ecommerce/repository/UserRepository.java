package com.arm.ecommerce.repository;

import com.arm.ecommerce.model.EcommerceUser;
import org.springframework.data.repository.CrudRepository;

/**
 * Provides CRUD operations for {@link EcommerceUser} entity.
 */
public interface UserRepository extends CrudRepository<EcommerceUser, Long> {
}
