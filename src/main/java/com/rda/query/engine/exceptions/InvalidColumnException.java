package com.rda.query.engine.exceptions;

public class InvalidColumnException extends RuntimeException {
    public InvalidColumnException(String errorMessage) {
        super(errorMessage);
    }
}
