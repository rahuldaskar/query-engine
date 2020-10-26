package com.rda.query.engine.exceptions;

public class InvalidSortQueryException extends RuntimeException {
    public InvalidSortQueryException(String errorMessage) {
        super(errorMessage);
    }
}
