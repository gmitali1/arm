package com.arm.coordinator.common;


import java.io.Serializable;

/**
 * Represents a  Promise that is sent from the Coordinator to the server in order to initiate
 * the execution of an operation.
 *
 * @author mitali ghotgalkar
 */

/**
 * A Promise represents a response to a proposal in the Paxos consensus algorithm.
 * It consists of a status (either ACCEPTED or REJECTED) and the proposal itself.
 */
public class Promise implements Serializable {

    private Status status;
    private Proposal proposal;

    /**
     * Default constructor for Promise class.
     */
    public Promise() {
    }

    /**
     * Constructor for creating a Promise with a given status and proposal.
     *
     * @param status   The status of the promise, which can be ACCEPTED or REJECTED.
     * @param proposal The proposal object that is being promised.
     */
    public Promise(Status status, Proposal proposal) {
        this.status = status;
        this.proposal = proposal;
    }

    /**
     * Returns the proposal object of the Promise.
     *
     * @return Proposal object of the Promise.
     */
    public Proposal getProposal() {
        return proposal;
    }

    /**
     * Sets the proposal object of the Promise.
     *
     * @param proposal The proposal object to be set.
     */
    public void setProposal(Proposal proposal) {
        this.proposal = proposal;
    }

    /**
     * Returns the status of the Promise, which can be ACCEPTED or REJECTED.
     *
     * @return The status of the Promise.
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Sets the status of the Promise.
     *
     * @param status The status to be set, which can be ACCEPTED or REJECTED.
     */
    public void setStatus(Status status) {
        this.status = status;
    }
}
