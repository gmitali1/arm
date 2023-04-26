package com.arm.ecommerce.controller;

import com.arm.ecommerce.model.Order;
import com.arm.ecommerce.model.OrderWrapper;
import com.arm.ecommerce.service.OrderService;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * The ServerController class is a REST controller that handles requests related to the server and orders.
 * It has endpoints for checking the server's status, getting all orders, and adding orders.
 */
@RestController
@RequestMapping("/api/server")
public class ServerController {

    private final OrderService orderService;

    /**
     * Constructs a new ServerController object with the given OrderService.
     *
     * @param orderService the OrderService used by the ServerController
     */
    public ServerController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Endpoint for checking if the server is alive.
     *
     * @return a ResponseEntity containing the message "Server is Alive" and a status code of 200 (OK)
     */
    @GetMapping
    public ResponseEntity<String> isServerAlive() {
        return new ResponseEntity<>("Server is Alive", HttpStatus.OK);
    }

    /**
     * Endpoint for getting all orders.
     *
     * @return an OrderWrapper object containing all orders of all users
     */
    @GetMapping(path = "/getAllOrders")
    @ResponseStatus(HttpStatus.OK)
    public @NotNull OrderWrapper getAllOrders() {
        return new OrderWrapper(orderService.getAllOrdersOfAllUsers());
    }

    /**
     * Endpoint for adding orders.
     *
     * @param orders an Iterable of Order objects to be added
     * @return a ResponseEntity containing a boolean value indicating whether the orders were successfully added
     */
    @PostMapping(path = "/addAllOrders")
    public ResponseEntity<Boolean> addAllOrders(@RequestBody Iterable<Order> orders) {
        Iterable<Order> responseOrders = orderService.addAllOrders(orders);
        if (responseOrders != null) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.ok(false);
        }
    }

}
