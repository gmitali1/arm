package com.arm.coordinator.repository;

import com.arm.coordinator.model.EcommerceUser;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<EcommerceUser, Long> {

    EcommerceUser getEcommerceUserByUsernameAndPassword(String username, String password);

}
