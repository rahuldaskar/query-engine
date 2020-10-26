package com.rda.query.engine.utils.validator;

import com.rda.query.engine.exceptions.InvalidQueryException;

import java.util.Arrays;
import java.util.List;

/**
 * Validates SELECT query
 */
public class BasicQueryValidator {

    public static boolean validate(String query) {
        isMissing(query);
        query = query.trim().replaceAll(";", "").toLowerCase();
        startsWithSelect(query);
        validateComplete(query);
        return true;
    }

    private static void isMissing(String query) {
        if (query == null || query.trim().isEmpty()) {
            throw new InvalidQueryException("Missing SELECT query!");
        }
    }

    private static void startsWithSelect(String query) {
        if (!query.trim().startsWith("select")) {
            throw new InvalidQueryException("Query should start with SELECT clause!");
        }
    }

    //SELECT column | columns | * FROM table [a valid table name in JSON] where col like | = val
    private static void validateComplete(String query) {
        List<String> queryTokens = Arrays.asList(query.split(" "));
        containsFrom(queryTokens);
    }

    private static void containsFrom(List<String> queryTokens) {
        if (!queryTokens.contains("from")) {
            throw new InvalidQueryException("Invalid SELECT query, missing 'FROM'!");
        }
    }

}
