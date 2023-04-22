package com.arm.coordinator.model;

import com.arm.coordinator.common.ResultCodeEnum;

import java.io.Serializable;

/**
 * Enum that stores the result after execution of put/get/delete on the key value store.
 *
 * @author mitali ghotgalkar
 */
public class Result implements Serializable {
    public static final long serialVersionUID = 1L;
    private boolean ok;
    private String message;
    private ResultCodeEnum resultCodeEnum;

    private Result() {
    }

    /**
     * returns result with ok status.
     *
     * @return Result for normal/positive execution.
     */
    public static Result ok() {
        Result result = new Result();
        result.ok = true;
        return result;
    }

    /**
     * sets the message of the result.
     *
     * @param message to be set.
     * @return Result with the message.
     */
    public Result message(String message) {
        this.message = message;
        return this;
    }

    /**
     * sets the result code enum of the result
     *
     * @param resultCodeEnum to be set.
     * @return Result with the result code enum set.
     */
    public static Result setResult(ResultCodeEnum resultCodeEnum) {
        Result result = new Result();
        result.resultCodeEnum = resultCodeEnum;
        return result;
    }

    /**
     * returns if the Result is ok or not.
     *
     * @return true/false.
     */
    public boolean isOk() {
        return ok;
    }

    /**
     * gets the message of the result object.
     *
     * @return message in string.
     */
    public String getMessage() {
        return message;
    }

    /**
     * gets the result code enum for this result object.
     *
     * @return ResultCodeEnum
     */
    public ResultCodeEnum getResult() {
        return resultCodeEnum;
    }

    @Override
    public String toString() {
        return "Result : ok = " + ok + ", message='" + message + ", resultCodeEnum=" + resultCodeEnum;
    }

}


