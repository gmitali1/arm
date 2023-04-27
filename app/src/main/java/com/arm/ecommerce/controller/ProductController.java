package com.arm.ecommerce.controller;

import com.arm.ecommerce.model.Product;
import com.arm.ecommerce.model.ProductPopulationRequest;
import com.arm.ecommerce.service.ProductService;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Controller for managing products in the e-commerce application.
 */
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    private final AtomicBoolean isServerReady;

    private String commitSecretKey;

    /**
     * Constructs a new instance of ProductController with the given ProductService.
     *
     * @param productService the ProductService to use for managing products
     */
    public ProductController(ProductService productService) {
        this.productService = productService;
        isServerReady = new AtomicBoolean(true);
        commitSecretKey = UUID.randomUUID().toString();
    }

    /**
     * Returns all products.
     *
     * @return an iterable of all products
     */
    @GetMapping
    public @NotNull Iterable<Product> getProducts() {
        return productService.getAllProducts();
    }

    /**
     * Prepares the server for a two-phase commit operation.
     *
     * @return a ResponseEntity with the commit secret key and status code 200 if the server is ready,
     * or a ResponseEntity with an error message and status code 409 if the server is busy
     */
    @GetMapping(path = "/prep")
    public ResponseEntity<String> prepPhase2PC() {
        if (isServerReady.get()) {
            return new ResponseEntity<>(commitSecretKey, HttpStatus.OK);
        }
        return new ResponseEntity<>("Server is busy", HttpStatus.CONFLICT);
    }

    /**
     * Commits a two-phase commit operation to save a list of products.
     *
     * @param productPopulationRequest the request object containing the list of products and commit key
     * @return a ResponseEntity with the result of the operation and status code 200 if successful,
     * or a ResponseEntity with an error message and status code 403 if the commit key is invalid,
     * or a ResponseEntity with an error message and status code 500 if an error occurred while saving products
     */
    @PostMapping(path = "/commit")
    public ResponseEntity<Boolean> commitPhase2PC(@RequestBody ProductPopulationRequest productPopulationRequest) {
        isServerReady.set(false);
        if (!commitSecretKey.equals(productPopulationRequest.getCommitKey())) {
            return new ResponseEntity<>(Boolean.FALSE, HttpStatus.FORBIDDEN);
        }
        Boolean hasCompletedSaving = productService.saveAll(productPopulationRequest.getProducts());
        commitSecretKey = UUID.randomUUID().toString();
        isServerReady.set(true);
        if (hasCompletedSaving) {
            return ResponseEntity.ok(Boolean.TRUE);
        } else {
            return new ResponseEntity<>(Boolean.FALSE, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}