package com.arm.ecommerce.model;

import com.arm.ecommerce.common.Promise;
import com.arm.ecommerce.common.Proposal;
import com.arm.ecommerce.common.Result;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

/**
 * This server represents the PAXOS server that is responsible for three phases : promise, accept and learn.
 * It also sets the failure percentage.
 *
 * @author mitali ghotgalkar
 */
public interface KeyValueServer extends Remote {

    /**
     * The promise phase is the first phase of the PAXOS protocol that is executed when a proposer wants to propose
     * a value to be agreed upon by a group of acceptors.
     *
     * @param proposal proposal to be proposed.
     * @return Promise object
     * @throws RemoteException
     */
    Promise promise(Proposal proposal) throws RemoteException;

    /**
     * During accept, the coordinator sends accept message to all servers with the proposed value
     *
     * @param proposal proposal proposed
     * @return accepted or not. (true/false).
     * @throws RemoteException
     */
    Boolean accept(Proposal proposal) throws RemoteException;

    /**
     * During learn, the coordinator asks all servers to commit to the chosen value.
     *
     * @param proposal proposal proposed
     * @return Result of learn phase (final phase).
     * @throws RemoteException
     */
    Result learn(Proposal proposal) throws RemoteException;

    /**
     * gets the failure percentage of promise
     *
     * @return failure promise percentage.
     * @throws RemoteException
     */
    double getPromisePercentage() throws RemoteException;

    /**
     * gets the failure percentage of accept
     *
     * @return failure accept percentage.
     * @throws RemoteException
     */
    double getAcceptPercentage() throws RemoteException;

    /**
     * gets the failure percentage of learn
     *
     * @return failure learn percentage.
     * @throws RemoteException
     */
    double getLearnPercentage() throws RemoteException;

    /**
     * get port of the server.
     *
     * @return port
     * @throws RemoteException
     */
    int getPort() throws RemoteException;

    /**
     * Creates a copy hashmap of key value pairs
     *
     * @return copy hashmap of key value pairs.
     * @throws RemoteException
     */
    Map<String, String> copyKeyValueStore() throws RemoteException;
}

