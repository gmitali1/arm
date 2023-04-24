package com.arm.ecommerce.service;

import com.arm.ecommerce.exception.ResourceNotFoundException;
import com.arm.ecommerce.model.Product;
import com.arm.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public boolean saveAll(Iterable<Product> products) {
        try {
            productRepository.saveAll(products);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
