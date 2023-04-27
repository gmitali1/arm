package com.arm.ecommerce.controller;

import com.arm.coordinator.common.OrderProductResponseObject;
import com.arm.coordinator.common.OrderResponseObject;
import com.arm.ecommerce.model.Order;
import com.arm.ecommerce.model.OrderProduct;
import com.arm.ecommerce.model.OrderStatus;
import com.arm.ecommerce.model.OrderWrapper;
import com.arm.ecommerce.service.OrderProductService;
import com.arm.ecommerce.service.OrderService;
import com.arm.ecommerce.service.ProductService;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code ServerController} class represents a REST API controller that handles server-related requests and responses.
 */
@RestController
@RequestMapping("/api/server")
public class ServerController {

    private final OrderService orderService;

    private final OrderProductService orderProductService;

    private final ProductService productService;

    /**
     * Constructs a new {@code ServerController} with the specified services.
     *
     * @param orderService        the {@code OrderService} instance that handles orders
     * @param orderProductService the {@code OrderProductService} instance that handles order products
     * @param productService      the {@code ProductService} instance that handles products
     */
    public ServerController(OrderService orderService, OrderProductService orderProductService, ProductService productService) {
        this.orderService = orderService;
        this.orderProductService = orderProductService;
        this.productService = productService;
    }

    /**
     * Returns a response entity indicating whether the server is alive.
     *
     * @return a response entity indicating whether the server is alive
     */
    @GetMapping
    public ResponseEntity<String> isServerAlive() {
        return new ResponseEntity<>("Server is Alive", HttpStatus.OK);
    }

    /**
     * Returns a wrapper object containing all orders of all users.
     *
     * @return a wrapper object containing all orders of all users
     */
    @GetMapping(path = "/getAllOrders")
    @ResponseStatus(HttpStatus.OK)
    public @NotNull OrderWrapper getAllOrders() {
        return new OrderWrapper(orderService.getAllOrdersOfAllUsers());
    }

    /**
     * Adds all orders specified in the request body and returns a response entity indicating success.
     *
     * @param orders an iterable of {@code OrderResponseObject} instances containing the orders to add
     * @return a response entity indicating success
     */
    @PostMapping(path = "/addAllOrders")
    public ResponseEntity<Boolean> addAllOrders(@RequestBody List<OrderResponseObject> orders) {
        // Make sure that all orders are in ascending order of their orders as they will be added on top
        orders.sort((o1, o2) -> (int) (o1.getId() - o2.getId()));

        // Iterate through each of them and add them to this DB
        for (OrderResponseObject orderResponseObject : orders) {
            Order order = new Order();
            order.setStatus(OrderStatus.PAID.name());
            order.setId(orderResponseObject.getId());
            order.setUserId(orderResponseObject.getUserId());
            order = this.orderService.create(order);

            List<OrderProduct> orderProducts = new ArrayList<>();
            for (OrderProductResponseObject orderProduct : orderResponseObject.getOrderProducts()) {
                orderProducts.add(orderProductService.create(new OrderProduct(order, productService.getProduct(orderProduct
                        .getProduct()
                        .getId()), orderProduct.getQuantity())));
            }

            order.setOrderProducts(orderProducts);

            this.orderService.update(order);
        }

        return ResponseEntity.ok(true);
    }

}
