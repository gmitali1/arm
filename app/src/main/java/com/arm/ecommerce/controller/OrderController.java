package com.arm.ecommerce.controller;

import com.arm.coordinator.common.Promise;
import com.arm.coordinator.common.Proposal;
import com.arm.coordinator.common.Result;
import com.arm.ecommerce.model.Order;
import com.arm.ecommerce.service.PaxosOrderService;
import com.arm.ecommerce.service.PaxosServer;
import jakarta.validation.constraints.NotNull;
import jakarta.websocket.server.PathParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;


/**
 * REST controller for handling orders.
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final PaxosServer<Order> paxosOrderService;

    /**
     * Constructs a new {@code OrderController} with the given {@code PaxosOrderService}.
     *
     * @param paxosOrderService the PaxosOrderService used to manage orders
     */
    public OrderController(PaxosOrderService paxosOrderService) {
        this.paxosOrderService = paxosOrderService;
    }

    /**
     * Retrieves a list of all orders.
     *
     * @return the list of all orders
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public @NotNull Iterable<Order> list(@PathParam("userId") Integer userId) {
        return this.paxosOrderService.findAll(userId);
    }

    /**
     * Promises to accept the given proposal for an order and returns a promise.
     *
     * @param proposal the proposal for the order
     * @return a promise
     */
    @PostMapping(path = "/promise")
    public ResponseEntity<Promise> promiseOrder(@RequestBody Proposal proposal) {
        Promise promise = paxosOrderService.promise(proposal);
        Logger.getLogger("OrderController").info(promise.toString());
        return ResponseEntity.ok(promise);
    }

    /**
     * Accepts the given proposal for an order and returns a response indicating whether the proposal was accepted or not.
     *
     * @param proposal the proposal for the order
     * @return a response indicating whether the proposal was accepted or not
     */
    @PostMapping(path = "/accept")
    public ResponseEntity<Boolean> acceptOrder(@RequestBody Proposal proposal) {
        return ResponseEntity.ok(paxosOrderService.accept(proposal));
    }

    /**
     * Learns the given proposal for an order and returns a result.
     *
     * @param proposal the proposal for the order
     * @return the result of learning the proposal
     * @throws IllegalStateException if unable to create the order
     */
    @PostMapping(path = "/learn")
    public ResponseEntity<Result> learnOrder(@RequestBody Proposal proposal, @PathParam("userId") Integer userId) {
        Result result = paxosOrderService.learn(proposal, userId);

        if (result.isOk()) {
            return ResponseEntity.ok(result);
        } else {
            throw new IllegalStateException("Unable to create Order");
        }
    }

}