package com.arm.coordinator.model;
import com.arm.coordinator.common.*;
import com.arm.ecommerce.model.Order;
import com.arm.ecommerce.model.Product;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

/**
 * Represents the Coordinator responsible for managing RMI Servers functioning as replicas to store data in the
 * Key Value Store. It performs the function of asynchronously writing data and reading data from the Key Value Store
 * and all of its replicas using consensus.
 *
 * @author mitali ghotgalkar
 */
public class Coordinator implements CoordinatorInterface {

    private final Set<Map.Entry<String, Integer>> servers;

    private final Logger coordinatorLogger = Logger.getLogger(this.getClass().getSimpleName());

    private final RestTemplate restTemplate;

    /**
     * Constructor to create coordinator with a set of servers.
     */
    public Coordinator() {
        this.servers = new HashSet<>();
        restTemplate = new RestTemplateBuilder().build();
    }

    private synchronized Result execute(Proposal proposal) {
        List<EcommerceServer> acceptors = new ArrayList<>();

        for (Map.Entry<String, Integer> server : this.servers) {
            try {
                String url = "http://"+server.getKey()+":"+server.getValue()+"/api/server";
                ResponseEntity<String> serverResponse = restTemplate.getForEntity(url, String.class);
                if (serverResponse.getStatusCode().is2xxSuccessful()) {
                    this.coordinatorLogger.info(String.format("Server at port %d Responded", server.getValue()));
                    acceptors.add(new EcommerceServer(server.getKey(), server.getValue()));
                } else {
                    this.coordinatorLogger.warning(String.format("Server at port %d down", server.getValue()));
                }
            } catch (Exception e) {
                this.coordinatorLogger.warning(String.format("Exception while trying to reach Server at port %d", server.getValue()));
                continue;
            }
        }

        int half = Math.floorDiv(acceptors.size(), 2) + 1;
        int promised = 0;

        switch (proposal.getOperation().getOperationType()) {
            case GET_ORDERS -> {
                Map<Iterable<Order>, Integer> valueMap = new HashMap<>();
                for (EcommerceServer acceptor : acceptors) {
                    String url = "http://" + acceptor.getHostname() + ":" + acceptor.getPort() + "/api/orders";
                    Iterable<Order> orders = restTemplate.getForObject(url, Iterable.class);
                    if (orders != null) {
                        coordinatorLogger.info("Response received from - "
                                + acceptor.getHostname() + ":" + acceptor.getPort());
                        valueMap.put(orders,
                                valueMap.getOrDefault(orders, 0) + 1);
                    }
                }

                for (Iterable<Order> value : valueMap.keySet()) {
                    if (valueMap.get(value) >= half) {
                        Result result = new Result();
                        result.setOk(true);
                        result.setMessage("Orders retrieved from distributed servers: " + value);
                        result.setOrders(value);
                        return result;
                    }
                }

                Result result = new Result();
                result.setResultCodeEnum(ResultCodeEnum.KEY_NOT_FOUND);
                result.setMessage(Response.ERROR.getMessage());
                return result;
            }

            case GET_PRODUCTS -> {
                Map<Iterable<Product>, Integer> valueMap = new HashMap<>();
                for (EcommerceServer acceptor : acceptors) {
                    String url = "http://" + acceptor.getHostname() + ":" + acceptor.getPort() + "/api/products";
                    Iterable<Product> products = restTemplate.getForObject(url, Iterable.class);
                    if (products != null) {
                        coordinatorLogger.info("Response received from - "
                                + acceptor.getHostname() + ":" + acceptor.getPort());
                        valueMap.put(products,
                                valueMap.getOrDefault(products, 0) + 1);
                    }
                }

                for (Iterable<Product> value : valueMap.keySet()) {
                    if (valueMap.get(value) >= half) {
                        Result result = new Result();
                        result.setOk(true);
                        result.setMessage("Products retrieved from distributed servers: " + value);
                        result.setProducts(value);
                        return result;
                    }
                }
                Result result = new Result();
                result.setResultCodeEnum(ResultCodeEnum.KEY_NOT_FOUND);
                result.setMessage(Response.ERROR.getMessage());
                return result;
            }

            case CREATE_ORDER -> {
                // Phase 1: Send the Promises
                for (EcommerceServer acceptor : acceptors) {
                    try {
                        String url = "http://" + acceptor.getHostname() + ":" + acceptor.getPort() + "/api/orders/promise";
                        ResponseEntity<Promise> promiseResponseEntity = restTemplate.postForEntity(url, proposal, Promise.class);
                        Promise promise = null;
                        if (promiseResponseEntity.getStatusCode().is2xxSuccessful()) {
                            promise = promiseResponseEntity.getBody();
                        }

                        if (promise == null) {
                            this.coordinatorLogger.info(String.format("Server at Port %d NOT RESPOND proposal %d", acceptor.getPort(), proposal.getId()));
                            this.coordinatorLogger.info(getCurrentTimeTillMillis(System.currentTimeMillis()));
                        } else if (promise.getStatus()== Status.PROMISED || promise.getStatus() == Status.ACCEPTED) {
                            promised++;
                            this.coordinatorLogger.info(String.format("Server at port %d PROMISED proposal %d", acceptor.getPort(), proposal.getId()));
                            this.coordinatorLogger.info(getCurrentTimeTillMillis(System.currentTimeMillis()));
                        } else {
                            this.coordinatorLogger.info(String.format("Server at port %d REJECTED proposal %d", acceptor.getPort(), proposal.getId()));
                            this.coordinatorLogger.info(getCurrentTimeTillMillis(System.currentTimeMillis()));
                        }
                    } catch (Exception e) {
                        this.coordinatorLogger.warning(String.format("Server at port %d DID NOT RESPOND proposal %d", acceptor.getPort(), proposal.getId()));
                        continue;
                    }
                }

                // Phase 2 - Send the "accept" message
                if (promised < half) {
                    Result result = new Result();
                    result.setResultCodeEnum(ResultCodeEnum.CONSENSUS_NOT_REACHED);
                    result.setMessage("Consensus not reached");
                    return result;
                } else {
                    coordinatorLogger.info("Promise Phase successfully completed.");
                }

                int accepted = 0;
                for (EcommerceServer acceptor : acceptors) {
                    try {
                        Boolean isAccepted = false;
                        String url = "http://" + acceptor.getHostname() + ":" + acceptor.getPort() + "/api/orders/accept";
                        ResponseEntity<Boolean> acceptResponseEntity = restTemplate.postForEntity(url, proposal, Boolean.class);
                        if (acceptResponseEntity.getStatusCode().is2xxSuccessful()) {
                            isAccepted = acceptResponseEntity.getBody();
                        }

                        if (isAccepted == null) {
                            this.coordinatorLogger.warning(String.format("Server at port %d DID NOT RESPOND proposal %d", acceptor.getPort(), proposal.getId()));
                            this.coordinatorLogger.info(getCurrentTimeTillMillis(System.currentTimeMillis()));
                        } else if (isAccepted) {
                            accepted++;
                            this.coordinatorLogger.info(String.format("Server at port %d ACCEPTED proposal %d", acceptor.getPort(), proposal.getId()));
                            this.coordinatorLogger.info(getCurrentTimeTillMillis(System.currentTimeMillis()));
                        }
                    } catch (Exception e) {
                        this.coordinatorLogger.info(getCurrentTimeTillMillis(System.currentTimeMillis()));
                        this.coordinatorLogger.warning(String.format("Server at port %d DID NOT RESPOND proposal %d", acceptor.getPort(), proposal.getId()));
                    }
                }

                if (accepted < half) {
                    Result result = new Result();
                    result.setResultCodeEnum(ResultCodeEnum.CONSENSUS_NOT_REACHED);
                    result.setMessage("Cannot reach consensus");
                    return result;
                } else {
                    coordinatorLogger.info("Accept Phase successfully completed.");
                }

                // Phase 3 - Send the "learn" message (this is extra credit)
                int learnt = 0;
                Result executionResult = null;
                for (EcommerceServer acceptor : acceptors) {
                    try {
                        Result result = null;
                        String url = "http://" + acceptor.getHostname() + ":" + acceptor.getPort() + "/api/orders/learn";
                        ResponseEntity<Object> resultResponseEntity = restTemplate.postForEntity(url, proposal, Object.class);
                        if (resultResponseEntity.getStatusCode().is2xxSuccessful()) {
                            result = new Result();
                            result.setOk(true);
//                            result.setOrder(resultResponseEntity.getBody());
                        }

                        if (result != null && result.isOk()) {
                            learnt++;
                            executionResult = result;
                        }

                    } catch (Exception e) {
                        coordinatorLogger.info("exception in learn phase");
                        coordinatorLogger.info(e.getLocalizedMessage());
                    }
                }

                if (learnt < half) {
                    Result result = new Result();
                    result.setResultCodeEnum(ResultCodeEnum.CONSENSUS_NOT_REACHED);
                    result.setMessage("Cannot reach consensus");
                    return result;
                } else {
                    coordinatorLogger.info("Learn Phase successfully completed.");
                }

                return executionResult;

            }

            case CREATE_PRODUCT -> {

            }

        }

        return null;
    }

    @Override
    public Result getAllOrders() {
        EcommerceOperation operation = new EcommerceOperation(OperationType.GET_ORDERS, null);
        Proposal proposal = Proposal.generateProposal(operation);
        return execute(proposal);
    }

    @Override
    public Result createOrder(OrderForm orderForm) {
        EcommerceOperation operation = new EcommerceOperation(OperationType.CREATE_ORDER, orderForm);
        Proposal proposal = Proposal.generateProposal(operation);
        return execute(proposal);
    }

    @Override
    public Result getAllProducts() {
        EcommerceOperation operation = new EcommerceOperation(OperationType.GET_PRODUCTS, null);
        Proposal proposal = Proposal.generateProposal(operation);
        return execute(proposal);
    }

    @Override
    public void addAcceptor(String hostName, int port) {
        coordinatorLogger.info("New acceptor got added at - " + hostName + ":" + port);
        coordinatorLogger.info(getCurrentTimeTillMillis(System.currentTimeMillis()));
        this.servers.add(Map.entry(hostName, port));
    }

    private static String getCurrentTimeTillMillis(long currentTimeMillis) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss:SS");
        Date date = new Date(currentTimeMillis);
        return simpleDateFormat.format(date);
    }

    private static class EcommerceServer {

        private final String hostname;
        private final int port;

        public EcommerceServer(String hostname, int port) {
            this.hostname = hostname;
            this.port = port;
        }

        public int getPort() {
            return port;
        }

        public String getHostname() {
            return hostname;
        }

    }
}

