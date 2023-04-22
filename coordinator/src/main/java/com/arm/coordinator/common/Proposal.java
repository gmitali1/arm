package com.arm.coordinator.common;

import java.io.Serializable;

/**
 * The Coordinator sends a Proposal to multiple servers to perform a write operation.
 *
 * @author mitali ghotgalkar
 */
public class Proposal implements Serializable {
    public static final long serialVersionUID = 1L;
    private long id;
    private KeyValueOperation operation;

    /**
     * Constructor for creating a Proposal with id and Key Value operation.
     *
     * @param id        id of the proposal.
     * @param operation operation that is proposed to the server.
     */
    public Proposal(long id, KeyValueOperation operation) {
        this.id = id;
        this.operation = operation;
    }

    /**
     * gets the id of proposal
     *
     * @return long id.
     */
    public long getId() {
        return id;
    }

    /**
     * Returns the operation
     *
     * @return operation
     */
    public KeyValueOperation getOperation() {
        return operation;
    }

    /**
     * sets the id of proposal.
     *
     * @param id id to be set.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * sets the operation of proposal.
     *
     * @param operation to be set.
     */
    public void setOperation(KeyValueOperation operation) {
        this.operation = operation;
    }

    /**
     * creates a new proposal with the key value operation passed to it.
     *
     * @param keyValueOperation
     * @return the created proposal.
     */
    public static synchronized Proposal generateProposal(KeyValueOperation keyValueOperation) {
        //String s = new SimpleDateFormat("HHmmssSSS").format(new Date());
        Proposal proposal = new Proposal(System.currentTimeMillis(), keyValueOperation);
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return proposal;
    }
}
 