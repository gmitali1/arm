package com.arm.service;
import com.arm.dao.ProductRepository;
import com.arm.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public Product findById(long id) {
        return productRepository.findById(id);
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
