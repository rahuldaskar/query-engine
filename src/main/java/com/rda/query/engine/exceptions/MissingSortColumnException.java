package com.rda.query.engine.exceptions;

public class MissingSortColumnException extends RuntimeException {
    public MissingSortColumnException(String errorMessage) {
        super(errorMessage);
    }
}
