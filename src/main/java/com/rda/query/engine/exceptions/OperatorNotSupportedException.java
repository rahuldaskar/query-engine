package com.rda.query.engine.exceptions;

public class OperatorNotSupportedException extends RuntimeException {
    public OperatorNotSupportedException(String errorMessage) {
        super(errorMessage);
    }
}
