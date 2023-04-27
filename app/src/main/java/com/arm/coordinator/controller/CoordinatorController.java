package com.arm.coordinator.controller;

import com.arm.coordinator.model.OrderForm;
import com.arm.coordinator.service.CoordinatorService;
import com.arm.ecommerce.model.Order;
import com.arm.ecommerce.model.Product;
import jakarta.validation.constraints.NotNull;
import jakarta.websocket.server.PathParam;
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
    public @NotNull Iterable<Order> getAllOrders(@PathParam("userId") int userId) {
        return coordinatorService.getAllOrders(userId);
    }

    @PostMapping("/orders")
    public ResponseEntity<Order> createOrder(@RequestBody OrderForm form, @PathParam("userId") int userId) {
        return coordinatorService.createOrder(form, userId);
    }

    @GetMapping(value = { "", "/products" })
    public @NotNull Iterable<Product> getProducts() { return coordinatorService.getAllProducts(); }

}
