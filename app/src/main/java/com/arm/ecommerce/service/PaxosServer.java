package com.arm.ecommerce.service;

import com.arm.coordinator.common.Promise;
import com.arm.coordinator.common.Proposal;
import com.arm.coordinator.common.Result;

/**
 * Represents a PAXOS Server Acceptor. The task of promise, accept and learn is carried out in the same server, and they
 * all act independently in their own threads.
 */
public interface PaxosServer<T> {

    /**
     * Creates a Promise from a Proposal that is passed to it.
     *
     * @param proposal pass a proposal to generate a promise at the server.
     * @return  promise generated at the server.
     */
    Promise promise(Proposal proposal);

    /**
     * Represents "accept" stage of execution of the PAXOS algorithm.
     *
     * @param proposal pass a proposal to accept it at the server.
     * @return  result of the accept stage in boolean format.
     */
    Boolean accept(Proposal proposal);

    /**
     * Represents "accept" stage of execution of the PAXOS algorithm. Announces the result of the complete operation to
     * the Coordinator.
     *
     * @param proposal pass a proposal to accept it at the server.
     * @return  result of the learn stage in boolean format.
     */
    Result learn(Proposal proposal);

    Iterable<T> findAll();


}
