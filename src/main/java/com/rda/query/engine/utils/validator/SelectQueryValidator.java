package com.rda.query.engine.utils.validator;

import com.rda.query.engine.exceptions.InvalidConditionException;
import com.rda.query.engine.exceptions.MissingConditionColumnException;
import com.rda.query.engine.model.Column;
import com.rda.query.engine.model.Condition;
import com.rda.query.engine.model.SelectQueryElements;
import com.rda.query.engine.model.Table;
import com.rda.query.engine.utils.CommonUtils;
import com.rda.query.engine.utils.JSONUtils;

import java.util.Arrays;
import java.util.List;

public class SelectQueryValidator {

    public static SelectQueryElements validate(String query, String playerTable, String worthTable) {
        query = query.trim().replaceAll(";", "").toLowerCase();
        CommonUtils.isMultiColCondition(query);
        playerTable = playerTable.toLowerCase();
        worthTable = worthTable.toLowerCase();
        List<String> queryTokens = Arrays.asList(query.split(" "));
        String table = CommonUtils.getTableName(queryTokens);
        String col = query.split("from")[0].trim().split("select")[1].trim();
        Table p = JSONUtils.getTableFromJSON(playerTable);
        Table w = JSONUtils.getTableFromJSON(worthTable);
        CommonUtils.isValidTable(table, p, w);
        Table t = table.equalsIgnoreCase(p.getSchema()) ? p : w;
        if (!col.equals("*")) {
            if (col.contains(",")) {
                Arrays.asList(col.split(",")).forEach(c -> CommonUtils.isValidCol(c.trim(), t));
            } else {
                CommonUtils.isValidCol(col.trim(), t);
            }
        }
        Column[] selectCols = CommonUtils.buildSelectColumns(col, t);

        if (queryTokens.contains("where")) {
            String conditionCol = query.split("where")[1].split(" ")[1];
            if (conditionCol == null || conditionCol.trim().isEmpty()) {
                throw new MissingConditionColumnException("No condition column found after WHERE!");
            }
            String operator = query.split("where")[1].split(" ")[2];
            if (conditionCol == null || conditionCol.trim().isEmpty()) {
                throw new InvalidConditionException("No condition operator found after column name: " + conditionCol + ". Please provide condition in format: <col> <operator> <val>. They should be space separated.");
            }
            CommonUtils.isValidCol(conditionCol, t);
            int condColIndex = CommonUtils.getColIndex(conditionCol, t);
            String value = query.split("where")[1].split(" ")[3];
            if (value == null || value.trim().isEmpty()) {
                throw new InvalidConditionException("No value found to be matched after condition operator: " + operator + ". Please provide condition in format: <col> <operator> <val>. They should be space separated.");
            }
            return new SelectQueryElements(t, selectCols, new Condition(new Column(conditionCol, condColIndex), operator, value));
        } else {
            return new SelectQueryElements(t, selectCols, null);
        }
    }

}
