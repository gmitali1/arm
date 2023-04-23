package com.arm.coordinator.controller;
import com.arm.coordinator.model.OrderForm;
import com.arm.coordinator.service.CoordinatorService;
import com.arm.ecommerce.model.Order;
import com.arm.ecommerce.model.Product;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.rmi.RemoteException;

@RestController
@RequestMapping("/api")
public class CoordinatorController {

    final CoordinatorService coordinatorService;

    public CoordinatorController(CoordinatorService coordinatorService) {
        this.coordinatorService = coordinatorService;
    }

    @GetMapping("/coordinator/register-server")
    @ResponseStatus(HttpStatus.OK)
    private ResponseEntity<String> addAcceptor(@RequestParam("hostName") String hostName, @RequestParam("port") int port)
            throws RemoteException {
        coordinatorService.addAcceptor(hostName, port);
        return new ResponseEntity<>("Acceptor Added", HttpStatus.OK);
    }

    @GetMapping("/orders")
    @ResponseStatus(HttpStatus.OK)
    public @NotNull Iterable<Order> getAllOrders() {
        return coordinatorService.getAllOrders();
    }

    @PostMapping("/orders")
    public ResponseEntity<Order> createOrder(@RequestBody OrderForm form) {
        return coordinatorService.createOrder(form);
    }

    @GetMapping(value = { "", "/products" })
    public @NotNull Iterable<Product> getProducts() { return coordinatorService.getAllProducts(); }

}
