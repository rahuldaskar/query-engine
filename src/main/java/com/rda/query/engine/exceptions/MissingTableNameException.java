package com.rda.query.engine.exceptions;

public class MissingTableNameException extends RuntimeException {
    public MissingTableNameException(String errorMessage) {
        super(errorMessage);
    }
}
