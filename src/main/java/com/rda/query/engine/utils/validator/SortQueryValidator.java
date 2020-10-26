package com.rda.query.engine.utils.validator;

import com.rda.query.engine.exceptions.InvalidSortOrderException;
import com.rda.query.engine.exceptions.InvalidSortQueryException;
import com.rda.query.engine.exceptions.MissingSortColumnException;
import com.rda.query.engine.model.Column;
import com.rda.query.engine.model.OrderBy;
import com.rda.query.engine.model.SortQueryElements;
import com.rda.query.engine.model.Table;
import com.rda.query.engine.utils.CommonUtils;
import com.rda.query.engine.utils.JSONUtils;

import java.util.Arrays;
import java.util.List;

public class SortQueryValidator {

    public static SortQueryElements validate(String query, String playerTable, String worthTable) {
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

        if (!query.contains(" order by ") && !query.contains(" order by")) {
            throw new InvalidSortQueryException("Invalid sort query, missing Order By clause!");
        }
        if (!(query.split(" order by ").length > 1) || !(query.split(" order by ")[1].split(" ").length > 0)) {
            throw new MissingSortColumnException("No sort column specified after ORDER BY clause!");
        }
        String sortCol = query.split(" order by ")[1].split(" ")[0];
        if (sortCol == null || sortCol.trim().isEmpty()) {
            throw new MissingSortColumnException("No sort column specified after ORDER BY clause!");
        }
        CommonUtils.isValidCol(sortCol, t);
        String sortingOrder = "ASC";
        if (query.split(" order by ")[1].split(" ").length == 2) {
            sortingOrder = query.split(" order by ")[1].split(" ")[1];
        }
        if ((sortingOrder != null && !sortingOrder.trim().isEmpty()) && (!sortingOrder.trim().equalsIgnoreCase("ASC") && !sortingOrder.trim().equalsIgnoreCase("DESC"))) {
            throw new InvalidSortOrderException("Only ASC and DESC should be the sorting order");
        }
        boolean asc = true;
        if (sortingOrder != null && !sortingOrder.trim().isEmpty()) {
            asc = sortingOrder.trim().equalsIgnoreCase("ASC") ? true : false;
        }

        return new SortQueryElements(t, selectCols, new OrderBy(new Column(sortCol, CommonUtils.getColIndex(sortCol, t)), asc));

    }

}
