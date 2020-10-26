package com.rda.query.engine.exceptions;

public class UnsupportedQueryException extends RuntimeException {
    public UnsupportedQueryException(String errorMessage) {
        super(errorMessage);
    }
}
