package com.rda.query.engine.exceptions;

public class MissingConditionColumnException extends RuntimeException {
    public MissingConditionColumnException(String errorMessage) {
        super(errorMessage);
    }
}
