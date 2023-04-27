package com.arm.coordinator.service;

import com.arm.coordinator.common.OrderResponseObject;
import com.arm.coordinator.common.ProductResponseObject;
import com.arm.coordinator.common.Result;
import com.arm.coordinator.model.Coordinator;
import com.arm.coordinator.model.CoordinatorInterface;
import com.arm.coordinator.model.OrderForm;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class CoordinatorService {

    CoordinatorInterface coordinatorInterface;
    RestTemplate restTemplate;
    private final List<ProductResponseObject> productList;

    public CoordinatorService() {
        productList = new ArrayList<>();
        populateProductList();
        coordinatorInterface = new Coordinator(productList);
        restTemplate = new RestTemplateBuilder().build();
    }

    private void populateProductList() {
        productList.add(new ProductResponseObject(1L, "TV Set", 300.00, "http://placehold.it/200x100"));
        productList.add(new ProductResponseObject(2L, "Game Console", 200.00, "http://placehold.it/200x100"));
        productList.add(new ProductResponseObject(3L, "Sofa", 100.00, "http://placehold.it/200x100"));
        productList.add(new ProductResponseObject(4L, "Ice-cream", 5.00, "http://placehold.it/200x100"));
        productList.add(new ProductResponseObject(5L, "Beer", 3.00, "http://placehold.it/200x100"));
        productList.add(new ProductResponseObject(6L, "Phone", 500.00, "http://placehold.it/200x100"));
        productList.add(new ProductResponseObject(7L, "Watch", 30.00, "http://placehold.it/200x100"));
    }

    public void addAcceptor(String hostName, int port) {
        coordinatorInterface.addAcceptor(hostName, port);
    }

    public Iterable<OrderResponseObject> getAllOrders(Integer userId) {
        // Do API Call for get all Orders and return all the orders
        Result ordersResult = coordinatorInterface.getAllOrders(userId);
        if (ordersResult.isOk()) {
            return ordersResult.getOrders();
        } else {
            throw new IllegalStateException("Unable to get products: " + ordersResult.getMessage());
        }
    }

    public synchronized ResponseEntity<OrderResponseObject> createOrder(OrderForm form, Integer userId) {
        // Execute create Order in the coordinator interface
        Result result = coordinatorInterface.createOrder(form, userId);
        if (result.isOk()) {
            return ResponseEntity.ok(new OrderResponseObject());
        }
        throw new IllegalStateException("Error Creating Order");
    }


    public Iterable<ProductResponseObject> getAllProducts(Integer userId) {
        // Execute Get Operation for all products on all servers
        Result productResult = coordinatorInterface.getAllProducts(userId);
        if (productResult.isOk()) {
            return productResult.getProducts();
        } else {
            throw new IllegalStateException("Unable to get products: " + productResult.getMessage());
        }
    }
}
