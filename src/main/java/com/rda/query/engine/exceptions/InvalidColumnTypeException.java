package com.rda.query.engine.exceptions;

public class InvalidColumnTypeException extends RuntimeException {
    public InvalidColumnTypeException(String errorMessage) {
        super(errorMessage);
    }
}
