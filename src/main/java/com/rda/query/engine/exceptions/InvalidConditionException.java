package com.rda.query.engine.exceptions;

public class InvalidConditionException extends RuntimeException {
    public InvalidConditionException(String errorMessage) {
        super(errorMessage);
    }
}
