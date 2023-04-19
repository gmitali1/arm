package com.arm.dao;

import com.arm.entity.Product;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@ComponentScan(basePackages = "com.arm")
@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

    List<Product> findByName(String name);

    Product findById(long id);
}
