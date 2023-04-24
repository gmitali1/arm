package com.arm.ecommerce.controller;

import com.arm.ecommerce.model.Product;
import com.arm.ecommerce.service.ProductService;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public @NotNull Iterable<Product> getProducts() {
        return productService.getAllProducts();
    }

    @PostMapping
    public ResponseEntity<Boolean> populateProducts(@RequestBody Iterable<Product> products) {
        return ResponseEntity.ok(productService.saveAll(products));
    }
}
