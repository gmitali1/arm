package com.arm.coordinator.model;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface for adding new acceptors which extends KeyValueStore.
 *
 * @author mitali ghotgalkar.
 */
public interface CoordinatorInterface extends Remote {

    /**
     * adds an acceptor with the provided hostname and port.
     *
     * @param hostName of the acceptor
     * @param port     of the acceptor
     * @throws RemoteException
     */
    void addAcceptor(String hostName, int port) throws RemoteException;

    public Result get(String key) throws RemoteException;

}

