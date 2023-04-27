package com.arm.ecommerce.service;

import com.arm.ecommerce.model.OrderProduct;
import com.arm.ecommerce.repository.OrderProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * OrderProductServiceImpl is a service class that provides implementation for OrderProductService interface.
 * It provides methods for creating and managing order products.
 * This class is marked with @Service and @Transactional annotations.
 */
@Service
@Transactional
public class OrderProductServiceImpl implements OrderProductService {

    private final OrderProductRepository orderProductRepository;

    /**
     * Constructs a new OrderProductServiceImpl object with the provided OrderProductRepository instance.
     *
     * @param orderProductRepository The OrderProductRepository instance.
     */
    public OrderProductServiceImpl(OrderProductRepository orderProductRepository) {
        this.orderProductRepository = orderProductRepository;
    }

    /**
     * Creates a new OrderProduct instance by saving the provided OrderProduct instance to the database.
     *
     * @param orderProduct The OrderProduct instance to be created.
     * @return The created OrderProduct instance.
     */
    @Override
    public OrderProduct create(OrderProduct orderProduct) {
        return this.orderProductRepository.save(orderProduct);
    }
}
