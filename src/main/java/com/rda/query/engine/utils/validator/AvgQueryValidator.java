package com.rda.query.engine.utils.validator;

import com.rda.query.engine.exceptions.InvalidConditionException;
import com.rda.query.engine.exceptions.InvalidQueryException;
import com.rda.query.engine.exceptions.MissingConditionColumnException;
import com.rda.query.engine.model.AvgQueryElements;
import com.rda.query.engine.model.Column;
import com.rda.query.engine.model.Condition;
import com.rda.query.engine.model.Table;
import com.rda.query.engine.utils.CommonUtils;
import com.rda.query.engine.utils.JSONUtils;

import java.util.Arrays;
import java.util.List;

public class AvgQueryValidator {

    public static AvgQueryElements validate(String query, String playerTable, String worthTable) {
        query = query.trim().replaceAll(";", "").toLowerCase();
        CommonUtils.isMultiColCondition(query);
        playerTable = playerTable.toLowerCase();
        worthTable = worthTable.toLowerCase();
        List<String> queryTokens = Arrays.asList(query.split(" "));
        containsAvg(queryTokens);
        String table = queryTokens.get(3);
        String col = queryTokens.get(1).split("\\(")[1].split("\\)")[0];
        Table p = JSONUtils.getTableFromJSON(playerTable);
        Table w = JSONUtils.getTableFromJSON(worthTable);
        CommonUtils.isValidTable(table, p, w);
        Table t = table.equalsIgnoreCase(p.getSchema()) ? p : w;
        CommonUtils.isValidCol(col, t);
        int colIndex = CommonUtils.getColIndex(col, t);
        CommonUtils.isNumericCol(colIndex, t);
        if (queryTokens.contains("where")) {
            String conditionCol = queryTokens.get(5);
            if (conditionCol == null || conditionCol.trim().isEmpty()) {
                throw new MissingConditionColumnException("No condition column found after WHERE!");
            }
            String operator = queryTokens.get(6);
            if (conditionCol == null || conditionCol.trim().isEmpty()) {
                throw new InvalidConditionException("No condition operator found after column name: " + conditionCol);
            }
            CommonUtils.isValidCol(conditionCol, t);
            int condColIndex = CommonUtils.getColIndex(conditionCol, t);
            String value = queryTokens.get(7);
            if (value == null || value.trim().isEmpty()) {
                throw new InvalidConditionException("No value found to be matched after condition operator: " + operator);
            }
            return new AvgQueryElements(t, new Column(col, colIndex), new Condition(new Column(conditionCol, condColIndex), operator, value));
        } else {
            return new AvgQueryElements(t, new Column(col, colIndex), null);
        }
    }


    private static void containsAvg(List<String> queryTokens) {
        if (!queryTokens.get(1).startsWith("avg(")) {
            throw new InvalidQueryException("Invalid SELECT query, missing 'avg'!");
        }
    }
}
