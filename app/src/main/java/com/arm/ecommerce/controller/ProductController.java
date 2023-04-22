package com.arm.ecommerce.controller;

import com.arm.ecommerce.model.Product;
import com.arm.ecommerce.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(path = "/product")
    public Product getProductById() {
        return productService.findById(1);
    }

    @GetMapping(path = "/populateStore")
    public ResponseEntity populateStore() {
        productService.populateProducts();
        return ResponseEntity.ok("population complete");
    }

    @GetMapping
    public @NotNull Iterable<Product> getProducts() { return productService.getAllProducts(); }
}
