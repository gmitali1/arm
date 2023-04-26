package com.arm.ecommerce.service;

import com.arm.ecommerce.model.Order;
import com.arm.ecommerce.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * The implementation of the {@link OrderService} interface.
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    /**
     * Constructs an instance of the {@link OrderServiceImpl} class with the given {@link OrderRepository}.
     *
     * @param orderRepository the order repository to use
     */
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /**
     * Gets all orders.
     *
     * @return an iterable of all orders
     */
    @Override
    public Iterable<Order> getAllOrders() {
        return this.orderRepository.findAll();
    }

    /**
     * Creates an order.
     *
     * @param order the order to create
     * @return the created order
     */
    @Override
    public Order create(Order order) {
        order.setDateCreated(LocalDate.now());

        return this.orderRepository.save(order);
    }

    /**
     * Updates an order.
     *
     * @param order the order to update
     */
    @Override
    public void update(Order order) {
        this.orderRepository.save(order);
    }

    /**
     * Gets all orders of all users.
     *
     * @return an iterable of all orders of all users
     */
    @Override
    public Iterable<Order> getAllOrdersOfAllUsers() {
        return this.orderRepository.findAll();
    }
}
