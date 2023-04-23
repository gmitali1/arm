package com.arm.coordinator.common;


import java.io.Serializable;

/**
 * Represents a  Promise that is sent from the Coordinator to the server in order to initiate
 * the execution of an operation.
 *
 * @author mitali ghotgalkar
 */

public class Promise implements Serializable {

    private Status status;

    private Proposal proposal;

    public Promise() {

    }

    /**
     * Used to create a Promise instance
     *
     * @param status   promise status which is an enum.
     * @param proposal proposal that contains the Key Value Operation.
     */
    public Promise(Status status, Proposal proposal) {
        this.status = status;
        this.proposal = proposal;
    }

    /**
     * Proposal object of the Promise.
     *
     * @return proposal object.
     */
    public Proposal getProposal() {
        return proposal;
    }

    /**
     * gets the status of the promise.
     *
     * @return status enum.
     */
    public Status getStatus() {
        return status;
    }

    public void setProposal(Proposal proposal) {
        this.proposal = proposal;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
