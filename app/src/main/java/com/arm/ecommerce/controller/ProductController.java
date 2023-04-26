package com.arm.ecommerce.controller;

import com.arm.coordinator.model.ProductPopulationRequest;
import com.arm.ecommerce.model.Product;
import com.arm.ecommerce.service.ProductService;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    private final AtomicBoolean isServerReady;

    private String commitSecretKey;

    public ProductController(ProductService productService) {
        this.productService = productService;
        isServerReady = new AtomicBoolean(true);
        commitSecretKey = UUID.randomUUID().toString();
    }

    @GetMapping
    public @NotNull Iterable<Product> getProducts() {
        return productService.getAllProducts();
    }

    @GetMapping(path = "/prep")
    public ResponseEntity<String> prepPhase2PC() {
        if (isServerReady.get()) {
            return new ResponseEntity<>(commitSecretKey, HttpStatus.OK);
        }
        return new ResponseEntity<>("Server is busy", HttpStatus.CONFLICT);
    }

    @PostMapping(path = "/commit")
    public ResponseEntity<Boolean> commitPhase2PC(@RequestBody ProductPopulationRequest productPopulationRequest) {
        isServerReady.set(false);
        if (!commitSecretKey.equals(productPopulationRequest.getCommitKey())) {
            return new ResponseEntity<>(Boolean.FALSE, HttpStatus.FORBIDDEN);
        }
        Boolean hasCompletedSaving = productService.saveAll(productPopulationRequest.getProducts());
        commitSecretKey = UUID.randomUUID().toString();
        isServerReady.set(true);
        return ResponseEntity.ok(hasCompletedSaving);
    }
}
