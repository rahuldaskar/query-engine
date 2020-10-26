package com.rda.query.engine.exceptions;

public class InvalidTableException extends RuntimeException {
    public InvalidTableException(String errorMessage) {
        super(errorMessage);
    }
}
