package com.arm.ecommerce.service;

import com.arm.ecommerce.model.Order;
import com.arm.ecommerce.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Iterable<Order> getAllOrders() {
        return this.orderRepository.findAll();
    }

    @Override
    public Order create(Order order) {
        order.setDateCreated(LocalDate.now());

        return this.orderRepository.save(order);
    }

    @Override
    public void update(Order order) {
        this.orderRepository.save(order);
    }

    @Override
    public Iterable<Order> addAllOrders(Iterable<Order> orders) {
        return orderRepository.saveAll(orders);
    }

    @Override
    public Iterable<Order> getAllOrdersOfAllUsers() {
        return this.orderRepository.findAll();
    }
}
