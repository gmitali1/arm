package com.arm.coordinator.model;

import com.arm.coordinator.common.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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

    private final List<ProductResponseObject> productList;

    private String productPopulationKey;

    private HashMap<Long, OrderForm> completedOrderForms;

    /**
     * Constructor to create coordinator with a set of servers.
     */
    public Coordinator(List<ProductResponseObject> productList) {
        this.servers = new HashSet<>();
        this.restTemplate = new RestTemplateBuilder().build();
        this.productList = productList;
        completedOrderForms = new HashMap<>();
    }

    private synchronized Result execute(Proposal proposal) {
        List<EcommerceServer> acceptors = new ArrayList<>();

        populateAcceptorsList(acceptors);

        int half = Math.floorDiv(acceptors.size(), 2) + 1;

        switch (proposal.getOperation().getOperationType()) {
            case GET_ORDERS -> {
                return getOrdersFromAllServers(acceptors, half);
            }

            case GET_PRODUCTS -> {
                return getProductsFromAllServers(acceptors, half);
            }

            case CREATE_ORDER -> {
                return perform3PhasePAXOSOrderCreation(proposal, acceptors, half);
            }

        }

        throw new IllegalArgumentException("Unsupported Operation Provided in Proposal, cannot proceed further");
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
    public synchronized void addAcceptor(String hostName, int port) {
        EcommerceServer server = new EcommerceServer(hostName, port);
        perform2PCPopulationOfNewlyAddedServer(server);
        if (performGossipAlgorithm(server)) {
            coordinatorLogger.info("Server orders check complete. " + server.getServerName() + " is up to date now.");
        }
        coordinatorLogger.info("New acceptor got added at - " + hostName + ":" + port);
        this.servers.add(Map.entry(hostName, port));
    }

    private Boolean performGossipAlgorithm(EcommerceServer newlyAddedServer) {
        coordinatorLogger.info("Checking for un-committed orders of " + newlyAddedServer.getServerName());
        List<EcommerceServer> acceptors = new ArrayList<>();
        populateAcceptorsList(acceptors);

        Set<OrderResponseObject> availableOrders = new HashSet<>();
        List<Long> missedOrdersIds = new ArrayList<>();
        for (OrderResponseObject order : getAllOrdersOfAServer(newlyAddedServer)) {
            availableOrders.add(order);
        }

        for (EcommerceServer acceptor : acceptors) {
            Iterable<OrderResponseObject> newOrders = getAllOrdersOfAServer(acceptor);
            for (OrderResponseObject order : newOrders) {
                if (!availableOrders.contains(order)) {
                    missedOrdersIds.add(order.getId());
                    coordinatorLogger.warning("Order #" + order.getId() + " not present on " + newlyAddedServer.getServerName());
                }
            }
        }

        if (missedOrdersIds.size() == 0) {
            return true;
        } else {
            return addOrdersToServer(newlyAddedServer,
                    missedOrdersIds
                            .stream()
                            .map(id -> completedOrderForms.get(id)).collect(Collectors.toList()));
        }

    }

    private Iterable<OrderResponseObject> getAllOrdersOfAServer(EcommerceServer server) {
        // Do rest call to get all orders of a server
        String url = "http://" + server.getHostname() + ":" + server.getPort() + "/api/server/getAllOrders";
        OrderResponseWrapperObject responseWrapperObject = restTemplate.getForObject(url, OrderResponseWrapperObject.class);
        if (responseWrapperObject != null) {
            return responseWrapperObject.getOrders();
        }

        return new ArrayList<>();
    }

    private Boolean addOrdersToServer(EcommerceServer server, Iterable<OrderForm> orders) {
        coordinatorLogger.info("Adding all missing orders to " + server.getServerName());
        // Do rest call to add orders
        String url = "http://" + server.getHostname() + ":" + server.getPort() + "/api/server/addAllOrders";
        return Boolean.TRUE.equals(restTemplate.postForObject(url, orders, Boolean.class));
    }

    private void perform2PCPopulationOfNewlyAddedServer(EcommerceServer server) {
        if (perform2PCPrepStage(server)) {
            if (perform2PCCommitStage(server)) {
                coordinatorLogger.info("Successfully populated server " + server.getServerName() + " with products.");
            } else {
                coordinatorLogger.severe("Failed to populate server " + server.getServerName() + " with products.");
            }
        } else {
            coordinatorLogger.severe("Failed to populate server " + server.getServerName() + " with products.");
        }
    }

    private synchronized boolean perform2PCPrepStage(EcommerceServer server) {
        try {
            coordinatorLogger.info("Sending Prepare message to " + server.getServerName());
            // Perform Is Server Ready Rest Call
            String url = "http://" + server.getHostname() + ":" + server.getPort() + "/api/products/prep";
            ResponseEntity<String> serverResponse = restTemplate.getForEntity(url, String.class);
            if (serverResponse.getStatusCode().is2xxSuccessful()) {
                this.coordinatorLogger.info(String.format("Server at port %d Responded", server.getPort()));
                this.productPopulationKey = serverResponse.getBody();
                return true;
            } else {
                this.coordinatorLogger.warning(String.format("Server at port %d down", server.getPort()));
                return false;
            }
        } catch (Exception e) {
            coordinatorLogger.info(server.getServerName() + " is down during prepare");
            return false;
        }
    }

    private synchronized boolean perform2PCCommitStage(EcommerceServer server) {
        try {
            coordinatorLogger.info("Executing Product Population operation on " + server.getServerName());
            // Perform Product List Population Rest Request
            String url = "http://" + server.getHostname() + ":" + server.getPort() + "/api/products/commit";
            return Boolean.TRUE.equals(
                    restTemplate.postForObject(url, new ProductPopulationRequest(productList, productPopulationKey),
                            Boolean.class));
        } catch (Exception e) {
            coordinatorLogger.info(server.getServerName() + " is down during commit");
            return false;
        }
    }

    private void populateAcceptorsList(List<EcommerceServer> acceptors) {
        for (Map.Entry<String, Integer> server : this.servers) {
            try {
                String url = "http://" + server.getKey() + ":" + server.getValue() + "/api/server";
                ResponseEntity<String> serverResponse = restTemplate.getForEntity(url, String.class);
                if (serverResponse.getStatusCode().is2xxSuccessful()) {
                    this.coordinatorLogger.info(String.format("Server at port %d Responded", server.getValue()));
                    acceptors.add(new EcommerceServer(server.getKey(), server.getValue()));
                } else {
                    this.coordinatorLogger.warning(String.format("Server at port %d down", server.getValue()));
                }
            } catch (Exception e) {
                this.coordinatorLogger.warning(String.format("Exception while trying to reach Server at port %d", server.getValue()));
            }
        }
    }

    @Nullable
    private Result perform3PhasePAXOSOrderCreation(Proposal proposal, List<EcommerceServer> acceptors, int half) {
        // Phase 1: Send the Promises
        int promised = performPromisePhase(proposal, acceptors);

        if (promised < half) {
            Result result = new Result();
            result.setResultCodeEnum(ResultCodeEnum.CONSENSUS_NOT_REACHED);
            result.setMessage("Consensus not reached");
            return result;
        } else {
            coordinatorLogger.info("Promise Phase successfully completed.");
        }

        // Phase 2 - Send the "accept" message
        int accepted = performAcceptPhase(proposal, acceptors);

        if (accepted < half) {
            Result result = new Result();
            result.setResultCodeEnum(ResultCodeEnum.CONSENSUS_NOT_REACHED);
            result.setMessage("Cannot reach consensus");
            return result;
        } else {
            coordinatorLogger.info("Accept Phase successfully completed.");
        }

        // Phase 3 - Send the "learn" message (this is extra credit)
        return performLearnPhaseAndAnnounceResult(proposal, acceptors, half);
    }

    private int performPromisePhase(Proposal proposal, List<EcommerceServer> acceptors) {
        int promised = 0;
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
                } else if (promise.getStatus() == Status.PROMISED || promise.getStatus() == Status.ACCEPTED) {
                    promised++;
                    this.coordinatorLogger.info(String.format("Server at port %d PROMISED proposal %d", acceptor.getPort(), proposal.getId()));
                } else {
                    this.coordinatorLogger.info(String.format("Server at port %d REJECTED proposal %d", acceptor.getPort(), proposal.getId()));
                }
            } catch (Exception e) {
                this.coordinatorLogger.warning(String.format("Server at port %d DID NOT RESPOND proposal %d", acceptor.getPort(), proposal.getId()));
            }
        }
        return promised;
    }

    private int performAcceptPhase(Proposal proposal, List<EcommerceServer> acceptors) {
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
                } else if (isAccepted) {
                    accepted++;
                    this.coordinatorLogger.info(String.format("Server at port %d ACCEPTED proposal %d", acceptor.getPort(), proposal.getId()));
                }
            } catch (Exception e) {
                this.coordinatorLogger.warning(String.format("Server at port %d DID NOT RESPOND proposal %d", acceptor.getPort(), proposal.getId()));
            }
        }
        return accepted;
    }

    @Nullable
    private Result performLearnPhaseAndAnnounceResult(Proposal proposal, List<EcommerceServer> acceptors, int half) {
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
                    completedOrderForms.put(proposal.getId(), proposal.getOperation().getOrderForm());
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

    @NotNull
    private Result getProductsFromAllServers(List<EcommerceServer> acceptors, int half) {
        Map<Iterable<ProductResponseObject>, Integer> valueMap = new HashMap<>();
        for (EcommerceServer acceptor : acceptors) {
            String url = "http://" + acceptor.getHostname() + ":" + acceptor.getPort() + "/api/products";
            Iterable<ProductResponseObject> products = restTemplate.getForObject(url, Iterable.class);
            if (products != null) {
                coordinatorLogger.info("Response received from - "
                        + acceptor.getHostname() + ":" + acceptor.getPort());
                valueMap.put(products,
                        valueMap.getOrDefault(products, 0) + 1);
            }
        }

        for (Iterable<ProductResponseObject> value : valueMap.keySet()) {
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

    @NotNull
    private Result getOrdersFromAllServers(List<EcommerceServer> acceptors, int half) {
        Map<Iterable<OrderResponseObject>, Integer> valueMap = new HashMap<>();
        for (EcommerceServer acceptor : acceptors) {
            String url = "http://" + acceptor.getHostname() + ":" + acceptor.getPort() + "/api/orders";
            Iterable<OrderResponseObject> orders = restTemplate.getForObject(url, Iterable.class);
            if (orders != null) {
                coordinatorLogger.info("Response received from - "
                        + acceptor.getHostname() + ":" + acceptor.getPort());
                valueMap.put(orders,
                        valueMap.getOrDefault(orders, 0) + 1);
            }
        }

        for (Iterable<OrderResponseObject> value : valueMap.keySet()) {
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

        public String getServerName() {
            return hostname + ":" + port;
        }

    }
}

