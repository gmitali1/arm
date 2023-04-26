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

@Service
@Transactional
public class PaxosOrderService implements PaxosServer<Order> {

    private final Logger serverLogger = Logger.getLogger(this.getClass().getSimpleName());

    private long maxId;

    private Proposal accepted;

    ProductService productService;

    OrderService orderService;

    OrderProductService orderProductService;

    public PaxosOrderService(ProductService productService, OrderService orderService, OrderProductService orderProductService) {
        this.productService = productService;
        this.orderService = orderService;
        this.orderProductService = orderProductService;
    }

    @Override
    public Iterable<Order> findAllByUserId(int userId) {
        return orderService.getAllOrdersByUserId(userId);
    }

    @Override
    public Promise promise(Proposal proposal) {
        this.serverLogger.info("Receive a promise message");

//        if (Math.random() <= promiseRandomFailurePercentage) {
//            this.serverLogger.info("Random failure triggered at promise phase : Proposal ID =
//            " +proposal.getId()+ ", Operation = " +proposal.getOperation());
//            return null;
//        }

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

    @Override
    public Boolean accept(Proposal proposal) {
        this.serverLogger.info("Received a accept message");

//        if (Math.random() <= acceptRandomFailurePercentage) {
//            this.serverLogger.info("Random failure triggered at accept phase : Proposal ID = " +proposal.getId()+ ", Operation = " +proposal.getOperation());
//            return null;
//        }

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

    @Override
    public Result learn(Proposal proposal) {
        this.serverLogger.info("Received a learn message");

//        if (Math.random() <= learnRandomFailurePercentage) {
//            this.serverLogger.info("Random failure triggered at learn phase : Proposal ID = " +proposal.getId()+ ", Operation = " +proposal.getOperation());
//            return Result.setResult(ResultCodeEnum.CONSENSUS_NOT_REACHED).message("Consensus not reached for learn phase.");
//        }

        EcommerceOperation operation = proposal.getOperation();
        Result result = null;
        OrderForm orderForm = operation.getOrderForm();
        OperationType type = operation.getOperationType();

        if (type == OperationType.CREATE_ORDER) {
            List<OrderProductDto> formDtos = orderForm.getProductOrders();
            validateProductsExistence(formDtos);
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
            order.setUserId(proposal.getOperation().getUserId());

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
