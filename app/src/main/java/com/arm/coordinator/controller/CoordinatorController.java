package com.arm.coordinator.controller;

import com.arm.coordinator.common.OrderResponseObject;
import com.arm.coordinator.common.ProductResponseObject;
import com.arm.coordinator.model.OrderForm;
import com.arm.coordinator.service.CoordinatorService;
import jakarta.validation.constraints.NotNull;
import jakarta.websocket.server.PathParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * The CoordinatorController class represents the REST controller for coordinating communication between acceptors and
 * clients in a distributed system.
 */
@RestController
@RequestMapping("/api")
public class CoordinatorController {

    /**
     * The CoordinatorService instance used by the controller.
     */
    final CoordinatorService coordinatorService;

    /**
     * Constructs a new instance of the CoordinatorController class with the specified CoordinatorService instance.
     *
     * @param coordinatorService The CoordinatorService instance to use.
     */
    public CoordinatorController(CoordinatorService coordinatorService) {
        this.coordinatorService = coordinatorService;
    }

    /**
     * Adds an acceptor with the specified hostname and port number to the distributed system.
     *
     * @param hostName The hostname of the acceptor to add.
     * @param port     The port number of the acceptor to add.
     * @return A ResponseEntity indicating that the acceptor was added successfully.
     */
    @GetMapping("/coordinator/register-server")
    @ResponseStatus(HttpStatus.OK)
    private ResponseEntity<String> addAcceptor(@RequestParam("hostName") String hostName, @RequestParam("port") int port) {
        coordinatorService.addAcceptor(hostName, port);
        return new ResponseEntity<>("Acceptor Added", HttpStatus.OK);
    }

    /**
     * Retrieves all orders in the distributed system.
     *
     * @return An Iterable containing all OrderResponseObject instances in the system.
     */
    @GetMapping("/orders")
    @ResponseStatus(HttpStatus.OK)
    public @NotNull Iterable<OrderResponseObject> getAllOrders(@PathParam("userId") Integer userId) {
        return coordinatorService.getAllOrders(userId);
    }

    /**
     * Creates a new order with the specified OrderForm data and adds it to the distributed system.
     *
     * @param form The OrderForm containing the data for the new order.
     * @return A ResponseEntity containing the OrderResponseObject representing the newly created order.
     */
    @PostMapping("/orders")
    public ResponseEntity<OrderResponseObject> createOrder(@RequestBody OrderForm form, @PathParam("userId") Integer userId) {
        return coordinatorService.createOrder(form, userId);
    }

    /**
     * Retrieves all products in the distributed system.
     *
     * @return An Iterable containing all ProductResponseObject instances in the system.
     */
    @GetMapping(value = {"", "/products"})
    public @NotNull Iterable<ProductResponseObject> getProducts(@PathParam("userId") Integer userId) {
        return coordinatorService.getAllProducts(userId);
    }

}
