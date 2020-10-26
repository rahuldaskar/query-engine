package com.rda.query.engine.processor;

import com.rda.query.engine.model.Column;
import com.rda.query.engine.model.QueryResult;
import com.rda.query.engine.model.SortQueryElements;
import com.rda.query.engine.utils.CommonUtils;
import com.rda.query.engine.utils.PrettyPrint;
import lombok.AllArgsConstructor;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortQueryProcessor {
    public static QueryResult process(SortQueryElements sortQueryElements) {
        String[] cols = CommonUtils.buildColsArray(sortQueryElements.getCols());
        Object[][] data = new Object[sortQueryElements.getTable().getData().size()][cols.length];
        int r = 0;
        int c = 0;
        Collections.sort(sortQueryElements.getTable().getData(), new ListComparator(sortQueryElements));
        for (List<Object> d : sortQueryElements.getTable().getData()) {
            for (Column column : sortQueryElements.getCols()) {
                data[r][c] = d.get(column.getIndex());
                c++;
            }
            r++;
            c = 0;
        }
        PrettyPrint.print(cols, data);
        return new QueryResult(cols, data);
    }

    @AllArgsConstructor
    static class ListComparator<T extends Comparable<Object>> implements Comparator<List<Object>> {

        private SortQueryElements sortQueryElements;

        @Override
        public int compare(List<Object> l1, List<Object> l2) {
            int sortColIndex = this.sortQueryElements.getOrderBy().getSortCol().getIndex();
            if (this.sortQueryElements.getTable().getTypes().get(sortColIndex).equalsIgnoreCase("integer")) {
                if (sortQueryElements.getOrderBy().isAsc()) {
                    return Double.compare((Double) l1.get(sortColIndex), (Double) l2.get(sortColIndex));
                } else {
                    return Double.compare((Double) l2.get(sortColIndex), (Double) l1.get(sortColIndex));
                }
            }
            if (sortQueryElements.getOrderBy().isAsc()) {
                return ((String) l1.get(sortColIndex)).compareTo((String) l2.get(sortColIndex));
            } else {
                return ((String) l2.get(sortColIndex)).compareTo((String) l1.get(sortColIndex));
            }
        }

    }
}
