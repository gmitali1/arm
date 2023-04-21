package com.arm.ecommerce.service;

import com.arm.ecommerce.exception.ResourceNotFoundException;
import com.arm.ecommerce.model.Product;
import com.arm.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Iterable<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProduct(long id) {
        return productRepository
          .findById(id)
          .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    public Product findById(long id) {
        var productOptional = productRepository.findById(id);
        if(productOptional.isPresent()) {
            return productOptional.get();
        }
        throw new IllegalArgumentException("Could not find the product for the specified id");
    }

    private void saveProduct(Product product) {
        productRepository.save(product);
    }

    public void saveProducts(List<Product> productList) {
        for(Product product: productList) {
            saveProduct(product);
        }
    }

    public void populateProducts() {
        Product product = new Product(Long.getLong("1"), "chocolate",Double.valueOf("10"),"https://www.google.com/");
        saveProduct(product);
    }
}
