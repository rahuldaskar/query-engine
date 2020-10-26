package com.rda.query.engine.exceptions;

public class InvalidQueryException extends RuntimeException {
    public InvalidQueryException(String errorMessage) {
        super(errorMessage);
    }
}
