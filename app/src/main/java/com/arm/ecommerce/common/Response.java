package com.arm.ecommerce.common;

/**
 * Enum for generating response after user input. This is used for logging purposes as well.
 *
 * @author mitali ghotgalkar
 */
public enum Response {
    SUCCESS("Operation successful"),
    ERROR("Operation failed, key not found"),
    INVALID("INVALID ENTRY, TRY AGAIN"),
    EMPTY("EMPTY TEXT, PLEASE TYPE SOME CHARACTERS"),
    UNEXPECTED("Some unexpected error occurred, try again"),
    FINDING_COORDINATOR("Client is looking for the PAXOS Coordinator"),
    COORDINATOR_FOUND("Successfully found the coordinator"),
    COORDINATOR_NOT_FOUND("Could not find the coordinator that you are looking for"),
    PREPARE_ERROR_PUT("Unexpected error occurred during Prepare for put"),
    PREPARE_ERROR_DELETE("Unexpected error occurred during Prepare for delete"),
    COMMIT_ERROR_PUT("Error occurred during commit for put"),
    COMMIT_ERROR_DELETE("Error occurred during commit for put"),
    INVALID_INPUT("please provide valid input, check README for details"),
    ARGUMENTS_ERROR("Please enter proper arguments while starting server"),
    INVALID_PERCENTAGE("Invalid failure percentage");
    final String message;

    Response(String s) {
        this.message = s;
    }

    /**
     * gets the message associated with the enum.
     *
     * @return message associated with the enum.
     */
    public String getMessage() {
        return message;
    }
}
