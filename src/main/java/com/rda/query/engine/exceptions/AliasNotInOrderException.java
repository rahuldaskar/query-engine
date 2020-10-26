package com.rda.query.engine.exceptions;

public class AliasNotInOrderException extends RuntimeException {
    public AliasNotInOrderException(String errorMessage) {
        super(errorMessage);
    }
}
