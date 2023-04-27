package com.arm.ecommerce.service;

import com.arm.coordinator.common.*;
import com.arm.coordinator.model.OrderForm;
import com.arm.ecommerce.dto.OrderProductDto;
import com.arm.ecommerce.exception.ResourceNotFoundException;
import com.arm.ecommerce.model.Order;
import com.arm.ecommerce.model.OrderProduct;
import com.arm.ecommerce.model.OrderStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Implementation of PaxosServer interface for handling Order objects. Implements the Paxos consensus algorithm to ensure that orders are only created once across a distributed system.
 */
@Service
@Transactional
public class PaxosOrderService implements PaxosServer<Order> {

    private final Logger serverLogger = Logger.getLogger(this.getClass().getSimpleName());

    private long maxId;

    private Proposal accepted;

    ProductService productService;

    OrderService orderService;

    OrderProductService orderProductService;

    /**
     * Constructs a new PaxosOrderService with the given dependencies.
     *
     * @param productService      a ProductService instance for handling product operations
     * @param orderService        an OrderService instance for handling order operations
     * @param orderProductService an OrderProductService instance for handling order product operations
     */
    public PaxosOrderService(ProductService productService, OrderService orderService, OrderProductService orderProductService) {
        this.productService = productService;
        this.orderService = orderService;
        this.orderProductService = orderProductService;
    }

    /**
     * Returns all orders of teh specified user.
     *
     * @return an Iterable of all orders
     */
    @Override
    public Iterable<Order> findAll(Integer userId) {
        return orderService.getAllOrders(userId);
    }

    /**
     * Generates a Promise for the given proposal. Implements the Paxos consensus algorithm.
     *
     * @param proposal a Proposal instance representing the proposed operation
     * @return a Promise instance
     */
    @Override
    public Promise promise(Proposal proposal) {
        this.serverLogger.info("Receive a promise message");

        ExecutorService executor = Executors.newSingleThreadExecutor();
        FutureTask<Promise> future = new FutureTask<>(() -> {
            if (proposal.getId() <= this.maxId) {
                return new Promise(Status.REJECTED, null);
            } else {
                this.maxId = proposal.getId();
                if (this.accepted != null) {
                    return new Promise(Status.ACCEPTED, new Proposal(this.accepted.getId(),
                            this.accepted.getOperation()));
                } else {
                    return new Promise(Status.PROMISED, proposal);
                }
            }
        });

        try {
            executor.submit(future);
            this.serverLogger.info("Promise successfully generated");
            return future.get(10, TimeUnit.SECONDS);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            this.serverLogger.severe("Something went wrong");
            return null;
        }

    }

    /**
     * Accepts the given proposal. Implements the Paxos consensus algorithm.
     *
     * @param proposal a Proposal instance representing the proposed operation
     * @return a Boolean indicating whether the proposal was accepted
     */
    @Override
    public Boolean accept(Proposal proposal) {
        this.serverLogger.info("Received a accept message");

        ExecutorService executor = Executors.newSingleThreadExecutor();
        FutureTask<Boolean> future = new FutureTask<>(() -> {
            if (proposal.getId() != this.maxId) {
                this.serverLogger.info("This is not the most recent promise, returning false");
                return false;
            }

            accepted = new Proposal(proposal.getId(), proposal.getOperation());
            this.serverLogger.info("Proposal successfully accepted");
            return true;

        });

        try {
            executor.submit(future);
            return future.get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            this.serverLogger.warning("Something went wrong");
            return null;
        }
    }

    /**
     * Learns the given proposal. Implements the Paxos consensus algorithm.
     *
     * @param proposal a Proposal instance representing the proposed operation
     * @param userId
     * @return a Result instance representing the result of the operation
     */
    @Override
    public Result learn(Proposal proposal, Integer userId) {
        this.serverLogger.info("Received a learn message");

        EcommerceOperation operation = proposal.getOperation();
        Result result = null;
        OrderForm orderForm = operation.getOrderForm();
        OperationType type = operation.getOperationType();

        if (type == OperationType.CREATE_ORDER) {
            List<OrderProductDto> formDtos = orderForm.getProductOrders();
            validateProductsExistence(formDtos);
            Order order = new Order();
            order.setStatus(OrderStatus.PAID.name());
            order = this.orderService.create(order);

            List<OrderProduct> orderProducts = new ArrayList<>();
            for (OrderProductDto dto : formDtos) {
                orderProducts.add(orderProductService.create(new OrderProduct(order, productService.getProduct(dto
                        .getProduct()
                        .getId()), dto.getQuantity())));
            }

            order.setOrderProducts(orderProducts);
            order.setUserId(userId);

            this.orderService.update(order);

            this.serverLogger.info("Order created successful");
            result = new Result();
            result.setOk(true);
//            result.setOrder(order);
            result.setResultCodeEnum(ResultCodeEnum.ALL_OKAY);
            result.setMessage("Order created successful");
        }

        return result;
    }

    /**
     * Validates whether each product in a list of OrderProductDto exists in the database by calling the getProduct
     * method on the productService object.
     *
     * @param orderProducts list of OrderProductDto to validate
     * @throws ResourceNotFoundException if a product in the list is not found in the database
     */
    private void validateProductsExistence(List<OrderProductDto> orderProducts) {
        List<OrderProductDto> list = orderProducts
                .stream()
                .filter(op -> Objects.isNull(productService.getProduct(op
                        .getProduct()
                        .getId())))
                .collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(list)) {
            throw new ResourceNotFoundException("Product not found");
        }
    }

}
