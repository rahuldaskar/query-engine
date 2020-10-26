package com.rda.query.engine.utils.validator;

import com.rda.query.engine.exceptions.AliasNotInOrderException;
import com.rda.query.engine.exceptions.InvalidQueryException;
import com.rda.query.engine.exceptions.UnsupportedQueryException;
import com.rda.query.engine.model.Column;
import com.rda.query.engine.model.JoinCondition;
import com.rda.query.engine.model.JoinConditionColumn;
import com.rda.query.engine.model.JoinQueryElements;
import com.rda.query.engine.model.Table;
import com.rda.query.engine.utils.CommonUtils;
import com.rda.query.engine.utils.JSONUtils;

import java.util.Arrays;
import java.util.List;

public class JoinQueryValidator {

    public static JoinQueryElements validate(String query, String playerTable, String worthTable) {
        query = query.trim().replaceAll(";", "").toLowerCase();
        playerTable = playerTable.toLowerCase();
        worthTable = worthTable.toLowerCase();
        Table p = JSONUtils.getTableFromJSON(playerTable);
        Table w = JSONUtils.getTableFromJSON(worthTable);
        containsJoin(query);
        containsOn(query);
        List<String> fromTokens = Arrays.asList(query.split(" from "));
        List<String> onTokens = Arrays.asList(fromTokens.get(1).split(" on "));
        List<String> tables = Arrays.asList(onTokens.get(0).split(" "));
        String table1 = tables.get(0).trim();
        String table1Alias = tables.get(1).trim();
        String table2 = tables.get(3).trim();
        String table2Alias = tables.get(4).trim();
        CommonUtils.isValidTable(table1, p, w);
        CommonUtils.isValidTable(table2, p, w);

        List<String> onCondition = Arrays.asList(onTokens.get(1).split("="));
        String conditionColAlias1 = onCondition.get(0).split("\\.")[0].trim();
        if (!conditionColAlias1.equalsIgnoreCase(table1Alias)) {
            throw new AliasNotInOrderException("First alias should be " + table1Alias + " in condition: " + Arrays.toString(onCondition.toArray()));
        }
        String conditionCol1 = onCondition.get(0).split("\\.")[1].trim();
        CommonUtils.isValidCol(conditionCol1, p);

        String conditionColAlias2 = onCondition.get(1).split("\\.")[0].trim();
        if (!conditionColAlias2.equalsIgnoreCase(table2Alias)) {
            throw new AliasNotInOrderException("First alias should be " + table2Alias + " in condition: " + Arrays.toString(onCondition.toArray()));
        }
        String conditionCol2 = onCondition.get(1).split("\\.")[1].split(" ")[0].trim();
        CommonUtils.isValidCol(conditionCol2, w);

        Column c1 = new Column(conditionCol1, CommonUtils.getColIndex(conditionCol1, p));
        Column c2 = new Column(conditionCol2, CommonUtils.getColIndex(conditionCol2, w));

        String col = query.split("from")[0].trim().split("select")[1].trim();

        if (col.startsWith("*") || col.contains(",")) {
            throw new UnsupportedQueryException("Only single select column is supported in JOIN condition");
        }

        JoinConditionColumn selectCol = null;
        if (col.startsWith(conditionColAlias1)) {
            CommonUtils.isValidCol(col.split("\\.")[1].trim(), p);
            selectCol = buildSelectColumns(col.split("\\.")[1].trim(), p);
        } else {
            CommonUtils.isValidCol(col.split("\\.")[1].trim(), w);
            selectCol = buildSelectColumns(col.split("\\.")[1].trim(), w);
        }

        if (query.contains("where")) {
            List<String> whereCondition = Arrays.asList(query.split("where"));
            if (whereCondition.get(1).contains(" or ")) {
                throw new UnsupportedOperationException("Only AND operator supported in JOIN condition");
            }

            List<String> andConditions = Arrays.asList(whereCondition.get(1).split("and"));
            JoinCondition[] conditions = buildConditions(andConditions, table1, table1Alias, p, table2, table2Alias, w);

            return new JoinQueryElements(selectCol, conditions, p, w, c1, c2);

        } else {
            return new JoinQueryElements(selectCol, null, p, w, c1, c2);
        }
    }

    private static JoinConditionColumn buildSelectColumns(String col, Table t) {
        return new JoinConditionColumn(col, CommonUtils.getColIndex(col, t), t);
    }

    private static JoinCondition[] buildConditions(List<String> andConditions, String table1, String table1Alias, Table p, String table2, String table2Alias, Table w) {
        JoinCondition[] conditions = new JoinCondition[andConditions.size()];
        for (int i = 0; i < andConditions.size(); i++) {
            String[] currentCond = andConditions.get(i).split(" ");
            JoinConditionColumn jCol = null;
            String colName = currentCond[1].split("\\.")[1];
            if (currentCond[1].trim().startsWith(table1Alias)) {
                jCol = new JoinConditionColumn(colName, CommonUtils.getColIndex(colName, p), p);
            } else {
                jCol = new JoinConditionColumn(colName, CommonUtils.getColIndex(colName, w), w);
            }

            if (!currentCond[2].trim().equals(">=") && !currentCond[2].trim().equals("<=") && !currentCond[2].trim().equals("=")) {
                throw new UnsupportedOperationException("Only >=, <= and = are supported.");
            }

            if (currentCond[2].trim().equals(">=") || currentCond[2].trim().equals("<=")) {
                CommonUtils.isNumericCol(jCol.getIndex(), jCol.getTable());
            }

            JoinCondition j = new JoinCondition(jCol, currentCond[2].trim(), currentCond[3].trim());
            conditions[i] = j;
        }

        return conditions;
    }

    private static void containsOn(String query) {
        if (!query.contains(" on ")) {
            throw new InvalidQueryException("Invalid JOIN query, missing ON keyword for JOIN condition!");
        }
    }

    private static void containsJoin(String query) {
        if (!query.contains(" join ")) {
            throw new InvalidQueryException("Invalid JOIN query, missing JOIN keyword!");
        }
    }

}
