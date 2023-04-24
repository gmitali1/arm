package com.arm.ecommerce.repository;

import com.arm.ecommerce.model.EcommerceUser;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<EcommerceUser, Long> {

}
