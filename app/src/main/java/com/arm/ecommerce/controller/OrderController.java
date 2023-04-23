package com.arm.ecommerce.controller;

import com.arm.coordinator.common.Promise;
import com.arm.coordinator.common.Proposal;
import com.arm.coordinator.common.Result;
import com.arm.ecommerce.model.Order;
import com.arm.ecommerce.service.PaxosOrderService;
import com.arm.ecommerce.service.PaxosServer;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final PaxosServer<Order> paxosOrderService;

    public OrderController(PaxosOrderService paxosOrderService) {
        this.paxosOrderService = paxosOrderService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public @NotNull Iterable<Order> list() {
        return this.paxosOrderService.findAll();
    }

    @PostMapping(path = "/promise")
    public ResponseEntity<Promise> promiseOrder(@RequestBody Proposal proposal) {
        Promise promise = paxosOrderService.promise(proposal);
        Logger.getLogger("OrderController").info(promise.toString());
        return ResponseEntity.ok(promise);
    }

    @PostMapping(path = "/accept")
    public ResponseEntity<Boolean> acceptOrder(@RequestBody Proposal proposal) {
        return ResponseEntity.ok(paxosOrderService.accept(proposal));
    }

    @PostMapping(path = "/learn")
    public ResponseEntity<Result> learnOrder(@RequestBody Proposal proposal) {
        Result result = paxosOrderService.learn(proposal);

        if (result.isOk()) {
            return ResponseEntity.ok(result);
        } else {
            throw new IllegalStateException("Unable to create Order");
        }
    }

}