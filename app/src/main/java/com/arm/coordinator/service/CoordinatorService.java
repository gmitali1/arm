package com.arm.coordinator.service;

import com.arm.coordinator.model.Coordinator;
import com.arm.coordinator.model.CoordinatorInterface;
import com.arm.coordinator.model.OrderForm;
import com.arm.ecommerce.model.Order;
import com.arm.ecommerce.model.Product;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CoordinatorService {

    CoordinatorInterface coordinatorInterface;
    RestTemplate restTemplate;

    public CoordinatorService() {
        coordinatorInterface = new Coordinator();
        restTemplate = new RestTemplateBuilder().build();
    }

    public void addAcceptor(String hostName, int port) {
        coordinatorInterface.addAcceptor(hostName,port);
    }

    public Iterable<Order> getAllOrders() {
        // Do API Call for get all Orders and return all the orders
        String url = "http://localhost:9090/api/orders";
        return restTemplate.getForObject(url, Iterable.class);
    }

    public ResponseEntity<Order> createOrder(OrderForm form) {
        // Execute create Order in the coordinator interface
        return null;
    }


    public Iterable<Product> getAllProducts() {
        // Execute Get Operation for all products on all servers
        String url = "http://localhost:9090/api/products";
        return restTemplate.getForObject(url, Iterable.class);
    }
}
