package com.arm.coordinator.controller;

import com.arm.coordinator.common.OrderResponseObject;
import com.arm.coordinator.common.ProductResponseObject;
import com.arm.coordinator.model.OrderForm;
import com.arm.coordinator.service.CoordinatorService;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CoordinatorController {

    final CoordinatorService coordinatorService;

    public CoordinatorController(CoordinatorService coordinatorService) {
        this.coordinatorService = coordinatorService;
    }

    @GetMapping("/coordinator/register-server")
    @ResponseStatus(HttpStatus.OK)
    private ResponseEntity<String> addAcceptor(@RequestParam("hostName") String hostName, @RequestParam("port") int port) {
        coordinatorService.addAcceptor(hostName, port);
        return new ResponseEntity<>("Acceptor Added", HttpStatus.OK);
    }

    @GetMapping("/orders")
    @ResponseStatus(HttpStatus.OK)
    public @NotNull Iterable<OrderResponseObject> getAllOrders() {
        return coordinatorService.getAllOrders();
    }

    @PostMapping("/orders")
    public ResponseEntity<OrderResponseObject> createOrder(@RequestBody OrderForm form) {
        return coordinatorService.createOrder(form);
    }

    @GetMapping(value = {"", "/products"})
    public @NotNull Iterable<ProductResponseObject> getProducts() {
        return coordinatorService.getAllProducts();
    }

}
