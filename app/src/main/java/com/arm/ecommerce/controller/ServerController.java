package com.arm.ecommerce.controller;

import com.arm.coordinator.model.OrderForm;
import com.arm.ecommerce.dto.OrderProductDto;
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
 * The ServerController class is a REST controller that handles requests related to the server and orders.
 * It has endpoints for checking the server's status, getting all orders, and adding orders.
 */
@RestController
@RequestMapping("/api/server")
public class ServerController {

    private final OrderService orderService;

    private final OrderProductService orderProductService;

    private final ProductService productService;

    /**
     * Constructs a new ServerController object with the given OrderService.
     *
     * @param orderService        the OrderService used by the ServerController
     * @param orderProductService
     * @param productService
     */
    public ServerController(OrderService orderService, OrderProductService orderProductService, ProductService productService) {
        this.orderService = orderService;
        this.orderProductService = orderProductService;
        this.productService = productService;
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
     * @return a ResponseEntity containing a boolean value indicating whether the orders were successfully added
     */
    @PostMapping(path = "/addAllOrders")
    public ResponseEntity<Boolean> addAllOrders(@RequestBody Iterable<OrderForm> orderForms) {
        for (OrderForm orderForm : orderForms) {
            List<OrderProductDto> formDtos = orderForm.getProductOrders();
            Order order = new Order();
            order.setStatus(OrderStatus.PAID.name());
            order.setId(orderForm.getOrderId());
            order = this.orderService.create(order);

            List<OrderProduct> orderProducts = new ArrayList<>();
            for (OrderProductDto dto : formDtos) {
                orderProducts.add(orderProductService.create(new OrderProduct(order, productService.getProduct(dto
                        .getProduct()
                        .getId()), dto.getQuantity())));
            }

            order.setOrderProducts(orderProducts);

            this.orderService.update(order);
        }
        return ResponseEntity.ok(true);
    }

}
