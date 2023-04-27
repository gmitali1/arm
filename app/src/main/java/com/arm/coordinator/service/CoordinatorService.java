package com.arm.coordinator.service;

import com.arm.coordinator.common.Result;
import com.arm.coordinator.model.Coordinator;
import com.arm.coordinator.model.CoordinatorInterface;
import com.arm.coordinator.model.OrderForm;
import com.arm.ecommerce.model.Order;
import com.arm.ecommerce.model.Product;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CoordinatorService {

    CoordinatorInterface coordinatorInterface;
    RestTemplate restTemplate;
    private final List<Product> productList;
    private final AtomicLong orderId;


    public CoordinatorService() {
        productList = new ArrayList<>();
        populateProductList();
        coordinatorInterface = new Coordinator(productList);
        restTemplate = new RestTemplateBuilder().build();
        orderId = new AtomicLong(1L);
    }

    private void populateProductList() {
        productList.add(new Product(1L, "TV Set", 300.00, "http://placehold.it/200x100"));
        productList.add(new Product(2L, "Game Console", 200.00, "http://placehold.it/200x100"));
        productList.add(new Product(3L, "Sofa", 100.00, "http://placehold.it/200x100"));
        productList.add(new Product(4L, "Ice-cream", 5.00, "http://placehold.it/200x100"));
        productList.add(new Product(5L, "Beer", 3.00, "http://placehold.it/200x100"));
        productList.add(new Product(6L, "Phone", 500.00, "http://placehold.it/200x100"));
        productList.add(new Product(7L, "Watch", 30.00, "http://placehold.it/200x100"));
    }

    public void addAcceptor(String hostName, int port) {
        coordinatorInterface.addAcceptor(hostName, port);
    }

    public Iterable<Order> getAllOrders(int userId) {
        // Do API Call for get all Orders and return all the orders
        Result ordersResult = coordinatorInterface.getAllOrders(userId);
        if (ordersResult.isOk()) {
            return ordersResult.getOrders();
        } else {
            throw new IllegalStateException("Unable to get products: " + ordersResult.getMessage());
        }
    }

    public synchronized ResponseEntity<Order> createOrder(OrderForm form, int userId) {
        // Execute create Order in the coordinator interface
        form.setOrderId(orderId.getAndIncrement());
        form.setUserId(userId);
        Result result = coordinatorInterface.createOrder(form);
        if (result.isOk()) {
            return ResponseEntity.ok(new Order());
        }
        throw new IllegalStateException("Error Creating Order");
    }


    public Iterable<Product> getAllProducts() {
        // Execute Get Operation for all products on all servers
        Result productResult = coordinatorInterface.getAllProducts();
        if (productResult.isOk()) {
            return productResult.getProducts();
        } else {
            throw new IllegalStateException("Unable to get products: " + productResult.getMessage());
        }
    }
}
