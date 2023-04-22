package com.arm.ecommerce.common;

/**
 * Enum for all Key value store operation names.
 *
 * @author mitali ghotgalkar
 */

public enum Operation {
    GET("GET"),
    PUT("PUT"),
    DELETE("DELETE"),
    PREP("PREP"),
    COMMIT("COMMIT");

    final String message;

    Operation(String s) {
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
