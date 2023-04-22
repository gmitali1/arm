package com.arm.ecommerce.common;

import java.io.Serializable;

/**
 * Operation that is to be carried out on the store (key-value store) and the key and value of the store.
 *
 * @author mitali ghotgalkar
 */
public class KeyValueOperation implements Serializable {
    public static final long serialVersionUID = 1L;
    private final OperationType operationType;
    private final String key;
    private final String value;

    /**
     * Constructor for creating Key Value Operation
     *
     * @param operationType the type of operation (put,get or delete)
     * @param key           key required for all the operations
     * @param value         value required to perform put operation.
     */
    public KeyValueOperation(OperationType operationType, String key, String value) {
        this.operationType = operationType;
        this.key = key;
        this.value = value;
    }

    /**
     * gives the key of the KeyValueOperation.
     *
     * @return key
     */
    public String getKey() {
        return key;
    }

    /**
     * gives the value of the KeyValueOperation.
     *
     * @return value
     */
    public String getValue() {
        return value;
    }

    /**
     * type of operation in enum
     *
     * @return operationType
     */
    public OperationType getOperationType() {
        return operationType;
    }
}
