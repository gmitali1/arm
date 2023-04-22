package com.arm.ecommerce.model;

import com.arm.ecommerce.common.*;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Logger;

/**
 * Key Value Store Server stores the key value store object and performs the desired operations
 * on the object (PUT,GET,DELETE). This stores one copy of a key-value data store.
 *
 * @author mitali ghotgalkar.
 */
public class Server extends UnicastRemoteObject implements KeyValueServer {

    private long maxId;
    private final int port;
    private final int coordinatorPort;
    private final String coordinatorHostName;
    private final String hostName;
    private Proposal accepted;
    private final Logger serverLogger = Logger.getLogger("ServerLogger");
    private final Map<String, String> keyValuePairs;
    private final double promisePercentage;
    private final double acceptPercentage;
    private final double learnPercentage;

    /**
     * Creates server with hostname, port, coordinator host and port, and the failure percentages.
     *
     * @param hostName            host
     * @param portNumber          port
     * @param coordinatorHostName coordinator host
     * @param coordinatorPort     coordinator port
     * @param promisePercentage   failure percentage for promise
     * @param acceptPercentage    failure percentage for accept
     * @param learnPercentage     failure percentage for learn
     * @throws RemoteException
     */
    public Server(String hostName, int portNumber, String coordinatorHostName, int coordinatorPort,
                  double promisePercentage, double acceptPercentage, double learnPercentage) throws RemoteException {
        keyValuePairs = new HashMap<>();
        this.hostName = hostName;
        this.port = portNumber;
        this.coordinatorHostName = coordinatorHostName;
        this.coordinatorPort = coordinatorPort;
        if (promisePercentage > 1.0 || promisePercentage < 0.0) {
            serverLogger.info(Response.INVALID_PERCENTAGE.getMessage());
            throw new IllegalArgumentException(Response.INVALID_PERCENTAGE.getMessage());
        }
        this.promisePercentage = promisePercentage;
        if (acceptPercentage > 1.0 || acceptPercentage < 0.0) {
            serverLogger.info(Response.INVALID_PERCENTAGE.getMessage());
            throw new IllegalArgumentException(Response.INVALID_PERCENTAGE.getMessage());
        }
        this.acceptPercentage = acceptPercentage;
        if (learnPercentage > 1.0 || learnPercentage < 0.0) {
            serverLogger.info(Response.INVALID_PERCENTAGE.getMessage());
            throw new IllegalArgumentException(Response.INVALID_PERCENTAGE.getMessage());
        }
        this.learnPercentage = learnPercentage;
    }

    public void registerServerWithCoordinator() {
            RestTemplate restTemplate = new RestTemplateBuilder().build();
            String uri = "http://" +coordinatorHostName+":"+coordinatorPort+"/api/coordinator/register-server?hostName="+hostName+"&&port="+port;
            ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
            System.out.println(result.getBody());
            if (result.getStatusCode().is2xxSuccessful()) {
                serverLogger.info("Registered server with coordinator");
            }
    }

    @Override
    public Promise promise(Proposal proposal) throws RemoteException {
        this.serverLogger.info("Receive a promise message");
        this.serverLogger.info(getCurrentTimeTillMillis(System.currentTimeMillis()));

        if (Math.random() <= promisePercentage) {
            this.serverLogger.info("Random failure triggered");
            this.serverLogger.info(getCurrentTimeTillMillis(System.currentTimeMillis()));
            return null;
        }

        ExecutorService executor = Executors.newSingleThreadExecutor();
        FutureTask<Promise> future = new FutureTask<>(() -> {
            if (proposal.getId() <= Server.this.maxId) {
                return new Promise(Status.REJECTED, null);
            } else {
                this.maxId = proposal.getId();
                if (this.accepted != null) {
                    return new Promise(Status.ACCEPTED, new Proposal(Server.this.accepted.getId(),
                            Server.this.accepted.getOperation()));
                } else {
                    return new Promise(Status.PROMISED, proposal);
                }
            }
        });

        try {
            executor.submit(future);
            return future.get(10, TimeUnit.SECONDS);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            this.serverLogger.warning("500");
            return null;
        }

    }

    @Override
    public Boolean accept(Proposal proposal) throws RemoteException {
        this.serverLogger.info("Receive an accept message");
        this.serverLogger.info(getCurrentTimeTillMillis(System.currentTimeMillis()));

        if (Math.random() <= acceptPercentage) {
            this.serverLogger.info("Random failure triggered - accept");
            this.serverLogger.info(getCurrentTimeTillMillis(System.currentTimeMillis()));
            return null;
        }

        ExecutorService executor = Executors.newSingleThreadExecutor();
        FutureTask<Boolean> future = new FutureTask<>(() -> {
            if (proposal.getId() != Server.this.maxId) {
                return false;
            }
            if (accepted == null) {
                accepted = new Proposal(proposal.getId(), proposal.getOperation());
            } else {
                accepted.setId(proposal.getId());
                accepted.setOperation(proposal.getOperation());
            }

            return true;

        });

        try {
            executor.submit(future);
            return future.get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            this.serverLogger.warning("500");
            return null;
        }

    }

    @Override
    public Result learn(Proposal proposal) throws RemoteException {
        this.serverLogger.info("Received a learn message");
        this.serverLogger.info(getCurrentTimeTillMillis(System.currentTimeMillis()));

        if (Math.random() <= learnPercentage) {
            this.serverLogger.info("Random failure triggered for learn");
            this.serverLogger.info(getCurrentTimeTillMillis(System.currentTimeMillis()));
            return Result.setResult(ResultCodeEnum.CONSENSUS_NOT_REACHED).message("Failure during learn phase");
        }

        KeyValueOperation operation = proposal.getOperation();
        Result result = null;
        String key = operation.getKey();
        String value = operation.getValue();
        OperationType type = operation.getOperationType();

        if (type == OperationType.DELETE) {
            if (this.keyValuePairs.containsKey(key)) {
                this.keyValuePairs.remove(key);
                result = Result.ok().message("DELETE operation successful");
            } else {
                result = Result.setResult(ResultCodeEnum.KEY_NOT_FOUND).message("Key not found");
            }
        } else if (type == OperationType.PUT) {
            this.keyValuePairs.put(key, value);
            result = Result.ok().message("PUT operation successful");
        }
        return result;
    }

    @Override
    public double getPromisePercentage() {
        return this.promisePercentage;
    }

    @Override
    public double getAcceptPercentage() {
        return this.acceptPercentage;
    }

    @Override
    public double getLearnPercentage() {
        return this.learnPercentage;
    }


    @Override
    public Map<String, String> copyKeyValueStore() throws RemoteException {
        return new HashMap<>(keyValuePairs);
    }

    @Override
    public int getPort() throws RemoteException {
        return port;
    }

    private static String getCurrentTimeTillMillis(long currentTimeMillis) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss:SS");
        Date date = new Date(currentTimeMillis);
        return simpleDateFormat.format(date);
    }
}

