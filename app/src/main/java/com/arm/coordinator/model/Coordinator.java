package com.arm.coordinator.model;

import com.arm.coordinator.common.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.logging.Logger;

/**
 * The Coordinator class implements the CoordinatorInterface and represents the coordinator of a distributed ecommerce system.
 * It is responsible for handling all the incoming requests from clients and coordinating the communication between the servers
 * to ensure consistency in data.
 */
public class Coordinator implements CoordinatorInterface {

    private final Set<Map.Entry<String, Integer>> servers;

    private final Logger coordinatorLogger = Logger.getLogger(this.getClass().getSimpleName());

    private final RestTemplate restTemplate;

    private final List<ProductResponseObject> productList;

    private String productPopulationKey;


    /**
     * Constructor to create a new Coordinator instance with a given list of products.
     * Initializes the servers set and the RestTemplate instance.
     *
     * @param productList List of all available products in the system
     */
    public Coordinator(List<ProductResponseObject> productList) {
        this.servers = new HashSet<>();
        this.restTemplate = new RestTemplateBuilder().build();
        this.productList = productList;
    }

    /**
     * Executes the given proposal by communicating with the acceptors and performing the necessary operations.
     * Uses the 3-phase Paxos protocol for order creation and the 2-phase commit protocol for other operations.
     *
     * @param proposal Proposal to be executed
     * @param userId   User ID of the User performing execution
     * @return Result object containing the result of the executed operation
     */
    private synchronized Result execute(Proposal proposal, Integer userId) {
        // Create a list of all available acceptors in the system
        List<EcommerceServer> acceptors = new ArrayList<>();
        populateAcceptorsList(acceptors);

        // Calculate the majority of acceptors needed for consensus
        int half = Math.floorDiv(acceptors.size(), 2) + 1;

        // Check the operation type and perform the necessary action
        switch (proposal.getOperation().getOperationType()) {
            case GET_ORDERS -> {
                return getOrdersFromAllServers(acceptors, userId);
            }
            case GET_PRODUCTS -> {
                return getProductsFromAllServers(acceptors);
            }
            case CREATE_ORDER -> {
                return perform3PhasePAXOSOrderCreation(proposal, acceptors, half, userId);
            }
        }

        // Throw an exception if the operation type is not supported
        throw new IllegalArgumentException("Unsupported Operation Provided in Proposal, cannot proceed further");
    }

    /**
     * Returns all orders from all servers in the system by using 2-phase commit protocol for consensus.
     *
     * @return Result object containing the list of all orders in the system
     */
    @Override
    public Result getAllOrders(Integer userId) {
        // Create a new operation for getting all orders and generate a proposal for it
        EcommerceOperation operation = new EcommerceOperation(OperationType.GET_ORDERS, null);
        Proposal proposal = Proposal.generateProposal(operation);

        // Execute the proposal and return the result
        return execute(proposal, userId);
    }

    /**
     * Creates a new order in the system by using 3-phase Paxos protocol for consensus.
     *
     * @param orderForm OrderForm object containing the details of the order to be created
     * @param userId    The unique identifier for the user
     * @return Result object containing the result of the order creation operation
     */
    @Override
    public Result createOrder(OrderForm orderForm, Integer userId) {
        // Create a new operation for creating the order and generate a proposal for it
        EcommerceOperation operation = new EcommerceOperation(OperationType.CREATE_ORDER, orderForm);
        Proposal proposal = Proposal.generateProposal(operation);
        return execute(proposal, userId);
    }

    @Override
    public Result getAllProducts(Integer userId) {
        EcommerceOperation operation = new EcommerceOperation(OperationType.GET_PRODUCTS, null);
        Proposal proposal = Proposal.generateProposal(operation);
        return execute(proposal, userId);
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
        Set<OrderResponseObject> missedOrdersIds = new HashSet<>();
        for (OrderResponseObject order : getAllOrdersOfAServer(newlyAddedServer)) {
            availableOrders.add(order);
        }

        for (EcommerceServer acceptor : acceptors) {
            Iterable<OrderResponseObject> newOrders = getAllOrdersOfAServer(acceptor);
            for (OrderResponseObject order : newOrders) {
                if (!availableOrders.contains(order)) {
                    missedOrdersIds.add(order);
                    coordinatorLogger.warning("Order #" + order.getId() + " not present on " + newlyAddedServer.getServerName());
                }
            }
        }

        if (missedOrdersIds.size() == 0) {
            return true;
        } else {
            return addOrdersToServer(newlyAddedServer, missedOrdersIds.stream().toList());
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

    private Boolean addOrdersToServer(EcommerceServer server, Iterable<OrderResponseObject> orders) {
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

    /**
     * Populates the list of acceptors with the servers that are currently up and running.
     *
     * @param acceptors an empty list of EcommerceServer objects to be populated with the active servers.
     */
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

    /**
     * Performs the 3-phase PAXOS protocol for order creation, using the given proposal and list of acceptors.
     *
     * @param proposal  the proposal for the order creation
     * @param acceptors the list of acceptors participating in the protocol
     * @param half      the minimum number of acceptors needed for consensus (half of the total acceptors + 1)
     * @param userId
     * @return the result of the protocol execution, including the order details if consensus was reached,
     * or an error message if consensus was not reached
     */
    @Nullable
    private Result perform3PhasePAXOSOrderCreation(Proposal proposal, List<EcommerceServer> acceptors, int half, Integer userId) {
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
        return performLearnPhaseAndAnnounceResult(proposal, acceptors, half, userId);
    }

    /**
     * This method performs the promise phase of the Paxos consensus algorithm.
     * It sends a proposal to all acceptors and waits for their promise or rejection responses.
     * If an acceptor promises or accepts the proposal, the method increments the promised count.
     *
     * @param proposal  the proposal to send to the acceptors
     * @param acceptors the list of acceptors to send the proposal to
     * @return the number of acceptors that promised or accepted the proposal
     */
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

    /**
     * Executes the 'accept' phase of the Two-Phase Commit protocol for a given proposal with the list of acceptors.
     * Sends an accept request to each acceptor with the given proposal and counts the number of accepted responses.
     *
     * @param proposal  The proposal to be accepted.
     * @param acceptors The list of acceptors to whom the accept request is sent.
     * @return The number of accepted responses.
     */
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


    /**
     * Performs the 'learn' phase and announces the result of the consensus.
     *
     * @param proposal  the proposed order to be learned
     * @param acceptors the list of acceptor servers
     * @param half      the quorum size needed for consensus
     * @param userId
     * @return the result of the execution if consensus is reached, or a {@code null} value otherwise
     */
    @Nullable
    private Result performLearnPhaseAndAnnounceResult(Proposal proposal, List<EcommerceServer> acceptors, int half, Integer userId) {
        int learnt = 0;
        Result executionResult = null;
        for (EcommerceServer acceptor : acceptors) {
            try {
                Result result = null;
                String url = "http://" + acceptor.getHostname() + ":" + acceptor.getPort() + "/api/orders/learn?userId=" + userId;
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

    @NotNull
    private Result getProductsFromAllServers(List<EcommerceServer> acceptors) {
        Random random = new Random();
        EcommerceServer acceptor = acceptors.get(random.nextInt(acceptors.size()));

        String url = "http://" + acceptor.getHostname() + ":" + acceptor.getPort() + "/api/products";
        Iterable<ProductResponseObject> products = restTemplate.getForObject(url, Iterable.class);
        if (products != null) {
            coordinatorLogger.info("Response received from - "
                    + acceptor.getHostname() + ":" + acceptor.getPort());
            Result result = new Result();
            result.setOk(true);
            result.setMessage("Products retrieved from distributed servers");
            result.setProducts(products);
            return result;
        }

        Result result = new Result();
        result.setResultCodeEnum(ResultCodeEnum.KEY_NOT_FOUND);
        result.setMessage(Response.ERROR.getMessage());
        return result;
    }

    @NotNull
    private Result getOrdersFromAllServers(List<EcommerceServer> acceptors, Integer userId) {
        Random random = new Random();
        EcommerceServer acceptor = acceptors.get(random.nextInt(acceptors.size()));

        String url = "http://" + acceptor.getHostname() + ":" + acceptor.getPort() + "/api/orders?userId=" + userId;
        Iterable<OrderResponseObject> orders = restTemplate.getForObject(url, Iterable.class);
        if (orders != null) {
            coordinatorLogger.info("Response received from - "
                    + acceptor.getHostname() + ":" + acceptor.getPort());
            Result result = new Result();
            result.setOk(true);
            result.setMessage("Orders retrieved from " + acceptor.getServerName() + " distributed servers");
            result.setOrders(orders);
            return result;
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

