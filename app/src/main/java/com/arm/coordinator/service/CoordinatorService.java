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

import java.util.concurrent.atomic.AtomicLong;

@Service
public class CoordinatorService {

    CoordinatorInterface coordinatorInterface;
    RestTemplate restTemplate;

    private AtomicLong orderId;

    public CoordinatorService() {
        coordinatorInterface = new Coordinator();
        restTemplate = new RestTemplateBuilder().build();
        orderId = new AtomicLong(1L);
    }

    public void addAcceptor(String hostName, int port) {
        coordinatorInterface.addAcceptor(hostName,port);
    }

    public Iterable<Order> getAllOrders() {
        // Do API Call for get all Orders and return all the orders
        Result ordersResult = coordinatorInterface.getAllOrders();
        if (ordersResult.isOk()) {
            return ordersResult.getOrders();
        } else {
            throw new IllegalStateException("Unable to get products: " + ordersResult.getMessage());
        }
    }

    public synchronized ResponseEntity<Order> createOrder(OrderForm form) {
        // Execute create Order in the coordinator interface
        form.setOrderId(orderId.getAndIncrement());
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
