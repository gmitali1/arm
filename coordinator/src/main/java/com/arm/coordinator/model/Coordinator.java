package com.arm.coordinator.model;
import com.arm.coordinator.common.*;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
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
public class Coordinator extends UnicastRemoteObject implements CoordinatorInterface {

    private final Set<Map.Entry<String, Integer>> servers;

    private final Logger coordinatorLogger;

    /**
     * Constructor to create coordinator with a set of servers.
     *
     * @throws RemoteException
     */
    public Coordinator() throws RemoteException {
        this.servers = new HashSet<>();
        this.coordinatorLogger = Logger.getLogger("Coordinator Logger");
    }

    private synchronized Result execute(Proposal proposal) throws RemoteException {
        List<KeyValueServer> acceptors = new ArrayList<>();

        for (Map.Entry<String, Integer> server : this.servers) {
            try {
                KeyValueServer acceptor = (KeyValueServer) Naming.lookup("rmi://" + server.getKey() +
                        ":" + server.getValue() + "/" + StringLiterals.KEYVALUESTORE.getLiteral());
                acceptors.add(acceptor);
            } catch (Exception e) {
                this.coordinatorLogger.warning(String.format("Warning : Server at port %d down", server.getValue()));
                continue;
            }
        }

        int half = Math.floorDiv(acceptors.size(), 2) + 1;
        int promised = 0;

        //for get operation, it checks for GET values of all servers and accordingly returns results.
        if (proposal.getOperation().getOperationType().equals(OperationType.GET)) {
            Map<String, Integer> valueMap = new HashMap<>();
            for (int i = 0; i < acceptors.size(); i++) {
                KeyValueServer server = acceptors.get(i);
                String answer;
                answer = server.copyKeyValueStore()
                        .getOrDefault(proposal.getOperation().getKey(), null);
                if (answer != null) {
                    valueMap.put(answer,
                            valueMap.getOrDefault(answer, 0) + 1);
                }
            }

            for (Iterator<String> iterator = valueMap.keySet().iterator(); iterator.hasNext(); ) {
                String value = iterator.next();
                if (valueMap.get(value) > servers.size() / 2) {
                    return Result.ok().message("Value from server is " + value);
                }
            }

            return Result.setResult(ResultCodeEnum.KEY_NOT_FOUND).message(Response.ERROR.getMessage());
        }

        // Phase 1: Send the Promises
        for (KeyValueServer acceptor : acceptors) {
            try {
                Promise promise = acceptor.promise(proposal);

                if (promise == null) {
                    this.coordinatorLogger.info(String.format("Server at Port %d NOT RESPOND proposal %d", acceptor.getPort(), proposal.getId()));
                    this.coordinatorLogger.info(getCurrentTimeTillMillis(System.currentTimeMillis()));
                } else if (promise.getStatus() == Status.PROMISED || promise.getStatus() == Status.ACCEPTED) {
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
            return Result.setResult(ResultCodeEnum.CONSENSUS_NOT_REACHED).message("Consensus not reached");
        }

        int accepted = 0;
        for (KeyValueServer acceptor : acceptors) {
            try {
                Boolean isAccepted = acceptor.accept(proposal);

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
            return Result.setResult(ResultCodeEnum.CONSENSUS_NOT_REACHED).message("Cannot reach consensus");
        }

        // Phase 3 - Send the "learn" message (this is extra credit)
        int learnt = 0;
        Result executionResult = null;
        for (KeyValueServer acceptor : acceptors) {
            try {
                Result result = acceptor.learn(proposal);
                if (result.isOk()) {
                    learnt++;
                    executionResult = result;
                }

            } catch (Exception e) {
                coordinatorLogger.info("exception in learn phase");
                coordinatorLogger.info(e.getLocalizedMessage());
            }
        }

        if (learnt < half) {
            return Result.setResult(ResultCodeEnum.CONSENSUS_NOT_REACHED).message("Cannot reach consensus");
        }

        return executionResult;
    }

    @Override
    public Result get(String key) throws RemoteException {
        KeyValueOperation operation = new KeyValueOperation(OperationType.GET, key, null);
        Proposal proposal = Proposal.generateProposal(operation);
        return execute(proposal);
    }

    @Override
    public void addAcceptor(String hostName, int port) throws RemoteException {
        coordinatorLogger.info("New acceptor got added at - " + hostName + ":" + port);
        coordinatorLogger.info(getCurrentTimeTillMillis(System.currentTimeMillis()));
        this.servers.add(Map.entry(hostName, port));
    }

    private static String getCurrentTimeTillMillis(long currentTimeMillis) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss:SS");
        Date date = new Date(currentTimeMillis);
        return simpleDateFormat.format(date);
    }
}

