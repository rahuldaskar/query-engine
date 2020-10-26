package com.rda.query.engine.exceptions;

public class InvalidSortOrderException extends RuntimeException {
    public InvalidSortOrderException(String errorMessage) {
        super(errorMessage);
    }
}
