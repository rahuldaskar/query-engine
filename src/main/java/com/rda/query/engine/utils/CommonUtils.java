package com.rda.query.engine.utils;

import com.rda.query.engine.exceptions.InvalidColumnException;
import com.rda.query.engine.exceptions.InvalidColumnTypeException;
import com.rda.query.engine.exceptions.InvalidTableException;
import com.rda.query.engine.exceptions.MissingTableNameException;
import com.rda.query.engine.exceptions.UnsupportedQueryException;
import com.rda.query.engine.model.Column;
import com.rda.query.engine.model.Table;

import java.util.List;

public class CommonUtils {


    public static void isValidCol(String col, Table t) {
        if (!t.getColumns().contains(col)) {
            throw new InvalidColumnException("Column with name: " + col + " does not exist in table:" + t.getSchema());
        }
    }

    public static void isNumericCol(int col, Table t) {
        if (!t.getTypes().get(col).equalsIgnoreCase("integer")) {
            throw new InvalidColumnTypeException("Column with name: " + t.getColumns().get(col) + " is not integer!");
        }
    }

    public static void isValidTable(String table, Table p, Table w) {
        if (!p.getSchema().equalsIgnoreCase(table) && !w.getSchema().equalsIgnoreCase(table)) {
            throw new InvalidTableException("Table with name: " + table + " does not exist!");
        }

    }


    public static void isMultiColCondition(String query) {
        if (query.contains("where") && query.split("where")[1].split(" ").length > 4) {
            throw new UnsupportedQueryException("Multi column condition is not supported as per current implementatin. Use only single column condition.");
        }
    }


    public static String getTableName(List<String> queryTokens) {
        for (int i = 0; i < queryTokens.size(); i++) {
            if (queryTokens.get(i).equalsIgnoreCase("FROM")) {
                if (queryTokens.get(i + 1) == null || queryTokens.get(i + 1).trim().isEmpty()) {
                    throw new MissingTableNameException("Missing table name in SELECT query!");
                }
                return queryTokens.get(i + 1);
            }
        }
        return null;
    }

    public static int getColIndex(String col, Table t) {
        int colIndex = 0;
        for (int i = 0; i < t.getColumns().size(); i++) {
            if (t.getColumns().get(i).equalsIgnoreCase(col)) {
                colIndex = i;
                break;
            }
        }
        return colIndex;
    }

    public static Column[] buildSelectColumns(String col, Table t) {
        Column[] cols = null;
        if (col.equals("*")) {
            cols = new Column[t.getColumns().size()];
            int index = 0;
            for (String c : t.getColumns()) {
                cols[index] = new Column(c, index);
                index++;
            }
            return cols;
        } else if (col.contains(",")) {
            String[] colTokens = col.split(",");
            cols = new Column[colTokens.length];
            int index = 0;
            for (String c : colTokens) {
                cols[index] = new Column(c, CommonUtils.getColIndex(c, t));
                index++;
            }
        } else {
            cols = new Column[1];
            cols[0] = new Column(col, CommonUtils.getColIndex(col, t));
        }
        return cols;
    }

    public static String[] buildColsArray(Column[] cols) {
        String[] colsArray = new String[cols.length];
        for (int i = 0; i < cols.length; i++) {
            colsArray[i] = cols[i].getName();
        }
        return colsArray;
    }
}
