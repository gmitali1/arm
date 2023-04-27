package com.arm.coordinator.common;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Class representing a proposal in the e-commerce system.
 *
 * @author mitali ghotgalkar
 */
public class Proposal {
    private long id;
    private EcommerceOperation operation;

    /**
     * Constructor for creating a Proposal with an id and a Key Value operation.
     *
     * @param id        the id of the proposal
     * @param operation the operation that is proposed to the server
     */
    public Proposal(long id, EcommerceOperation operation) {
        this.id = id;
        this.operation = operation;
    }

    /**
     * Default constructor for creating a Proposal.
     */
    public Proposal() {

    }

    /**
     * Creates a new proposal with the Key Value operation passed to it and a unique id generated using the current
     * system time in milliseconds.
     *
     * @param ecommerceOperation the Key Value operation to be proposed
     * @return the created proposal
     */
    @JsonIgnore
    public static synchronized Proposal generateProposal(EcommerceOperation ecommerceOperation) {
        Proposal proposal = new Proposal(System.currentTimeMillis(), ecommerceOperation);
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return proposal;
    }

    /**
     * Returns the id of the proposal.
     *
     * @return the id of the proposal
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the id of the proposal.
     *
     * @param id the id to be set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Returns the operation proposed in the proposal.
     *
     * @return the operation proposed in the proposal
     */
    public EcommerceOperation getOperation() {
        return operation;
    }

    /**
     * Sets the operation proposed in the proposal.
     *
     * @param operation the operation to be set
     */
    public void setOperation(EcommerceOperation operation) {
        this.operation = operation;
    }
}
 