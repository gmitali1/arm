package com.arm.coordinator.common;

/**
 * Enum specifying the coordinator and server binding string values.
 *
 * @author mitali ghotgalkar
 */
public enum StringLiterals {
    KEYVALUESTORE("keyValueStore"),
    SERVER("KeyValueStoreServer");

    /**
     * gets the literal associated with the enum.
     *
     * @return literal associated with the enum.
     */
    public String getLiteral() {
        return literal;
    }

    final String literal;

    StringLiterals(String literal) {
        this.literal = literal;
    }
}
