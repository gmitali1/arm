package com.arm.ecommerce.controller;

import com.arm.ecommerce.model.Product;
import com.arm.ecommerce.service.ProductService;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public @NotNull Iterable<Product> getProducts() { return productService.getAllProducts(); }
}
