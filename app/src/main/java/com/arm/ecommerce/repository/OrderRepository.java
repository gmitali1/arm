package com.arm.ecommerce.repository;

import com.arm.ecommerce.model.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {

    Iterable<Order> findOrdersByUserId(int userId);

}
