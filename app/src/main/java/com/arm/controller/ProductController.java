package com.arm.controller;

import com.arm.entity.Product;
import com.arm.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@ComponentScan(basePackages = "com.arm")
@RestController
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping(path = "/product")
    public Product getProductById() {
        return productService.findById(1);
    }

    @GetMapping(path = "/populateStore")
    public ResponseEntity populateStore() {
        productService.populateProducts();
        return ResponseEntity.ok("population complete");
    }
}
