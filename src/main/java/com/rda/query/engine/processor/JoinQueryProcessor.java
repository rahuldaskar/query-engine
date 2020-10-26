package com.rda.query.engine.processor;

import com.rda.query.engine.model.JoinCondition;
import com.rda.query.engine.model.JoinQueryElements;
import com.rda.query.engine.model.QueryResult;
import com.rda.query.engine.utils.PrettyPrint;

import java.util.List;

public class JoinQueryProcessor {
    public static QueryResult process(JoinQueryElements joinQueryElements) {
        String[] cols = new String[1];
        cols[0] = joinQueryElements.getSelectCol().getTable().getSchema() + "." + joinQueryElements.getSelectCol().getName();
        Object[][] data = null;

        if (joinQueryElements.getJoinConditions() != null) {
            //build data size
            int dataSize = 0;
            for (List<Object> t1Data : joinQueryElements.getTable1().getData()) {
                for (List<Object> t2Data : joinQueryElements.getTable2().getData()) {
                    if (t1Data.get(joinQueryElements.getC1().getIndex()).equals(t2Data.get(joinQueryElements.getC2().getIndex())) && evaluateCondition(t1Data, t2Data, joinQueryElements)) {
                        dataSize++;
                    }
                }
            }

            //build data
            data = new Object[dataSize][cols.length];
            int r = 0;
            int c = 0;
            for (List<Object> t1Data : joinQueryElements.getTable1().getData()) {
                for (List<Object> t2Data : joinQueryElements.getTable2().getData()) {
                    if (t1Data.get(joinQueryElements.getC1().getIndex()).equals(t2Data.get(joinQueryElements.getC2().getIndex())) && evaluateCondition(t1Data, t2Data, joinQueryElements)) {
                        if (joinQueryElements.getSelectCol().getTable().getSchema().equalsIgnoreCase(joinQueryElements.getTable1().getSchema())) {
                            data[r][c] = t1Data.get(joinQueryElements.getSelectCol().getIndex());
                        } else {
                            data[r][c] = t2Data.get(joinQueryElements.getSelectCol().getIndex());
                        }
                        r++;
                        c = 0;
                    }
                }
            }
        } else {
            //build data size
            int dataSize = 0;
            for (List<Object> t1Data : joinQueryElements.getTable1().getData()) {
                for (List<Object> t2Data : joinQueryElements.getTable2().getData()) {
                    if (t1Data.get(joinQueryElements.getC1().getIndex()).equals(t2Data.get(joinQueryElements.getC2().getIndex()))) {
                        dataSize++;
                    }
                }
            }

            //build data
            data = new Object[dataSize][cols.length];
            int r = 0;
            int c = 0;
            for (List<Object> t1Data : joinQueryElements.getTable1().getData()) {
                for (List<Object> t2Data : joinQueryElements.getTable2().getData()) {
                    if (t1Data.get(joinQueryElements.getC1().getIndex()).equals(t2Data.get(joinQueryElements.getC2().getIndex()))) {
                        if (joinQueryElements.getSelectCol().getTable().getSchema().equalsIgnoreCase(joinQueryElements.getTable1().getSchema())) {
                            data[r][c] = t1Data.get(joinQueryElements.getSelectCol().getIndex());
                        } else {
                            data[r][c] = t2Data.get(joinQueryElements.getSelectCol().getIndex());
                        }
                        r++;
                        c = 0;
                    }
                }
            }
        }
        PrettyPrint.print(cols, data);
        return new QueryResult(cols, data);
    }

    private static boolean evaluateCondition(List<Object> t1Data, List<Object> t2Data, JoinQueryElements joinQueryElements) {
        boolean result = true;
        for (JoinCondition jc : joinQueryElements.getJoinConditions()) {
            if (jc.getOperator().equals("=")) {
                if (jc.getCol().getTable().getSchema().equalsIgnoreCase(joinQueryElements.getTable1().getSchema())) {
                    if (joinQueryElements.getTable1().getTypes().get(jc.getCol().getIndex()).equalsIgnoreCase("String")) {
                        result = result && (t1Data.get(jc.getCol().getIndex()).equals(jc.getValue().replace("'", "")));
                    } else {
                        result = result && (t1Data.get(jc.getCol().getIndex()) == jc.getValue());
                    }
                } else {
                    if (joinQueryElements.getTable2().getTypes().get(jc.getCol().getIndex()).equalsIgnoreCase("String")) {
                        result = result && (t2Data.get(jc.getCol().getIndex()).equals(jc.getValue().replace("'", "")));
                    } else {
                        result = result && (t2Data.get(jc.getCol().getIndex()) == jc.getValue());
                    }
                }
            } else if (jc.getOperator().equals(">=")) {
                if (jc.getCol().getTable().getSchema().equalsIgnoreCase(joinQueryElements.getTable1().getSchema())) {
                    result = result && ((Double) t1Data.get(jc.getCol().getIndex()) >= Double.parseDouble(jc.getValue()));
                } else {
                    result = result && ((Double) t2Data.get(jc.getCol().getIndex()) >= Double.parseDouble(jc.getValue()));
                }
            } else {
                if (jc.getCol().getTable().getSchema().equalsIgnoreCase(joinQueryElements.getTable1().getSchema())) {
                    result = result && ((Double) t1Data.get(jc.getCol().getIndex()) <= Double.parseDouble(jc.getValue()));
                } else {
                    result = result && ((Double) t2Data.get(jc.getCol().getIndex()) <= Double.parseDouble(jc.getValue()));
                }
            }
        }
        return result;
    }

}
