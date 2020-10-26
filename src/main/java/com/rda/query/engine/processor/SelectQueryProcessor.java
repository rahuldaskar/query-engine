package com.rda.query.engine.processor;

import com.rda.query.engine.exceptions.OperatorNotSupportedException;
import com.rda.query.engine.model.Column;
import com.rda.query.engine.model.QueryResult;
import com.rda.query.engine.model.SelectQueryElements;
import com.rda.query.engine.utils.CommonUtils;
import com.rda.query.engine.utils.PrettyPrint;

import java.util.List;

public class SelectQueryProcessor {
    //only supports like and = condition on 'single' col
    //col like %abcd or col like abcd% or col like %abcd%
    //col = 'val'
    public static QueryResult process(SelectQueryElements selectQueryElements) {

        String[] cols = CommonUtils.buildColsArray(selectQueryElements.getCols());
        Object[][] data = null;

        if (selectQueryElements.getCondition() != null) {
            String operator = selectQueryElements.getCondition().getOperator();

            if (!selectQueryElements.getCondition().getOperator().equalsIgnoreCase("like") && !selectQueryElements.getCondition().getOperator().equals("=")) {
                throw new OperatorNotSupportedException("Only LIKE and = condition is supported for current implementation!");
            }

            if (operator.equalsIgnoreCase("like")) {
                if (selectQueryElements.getCondition().getValue().startsWith("'%") && selectQueryElements.getCondition().getValue().endsWith("%'")) {
                    //build data size
                    int dataSize = 0;
                    String val = selectQueryElements.getCondition().getValue().split("%")[1];
                    for (List<Object> d : selectQueryElements.getTable().getData()) {
                        if (d.get(selectQueryElements.getCondition().getCol().getIndex()).toString().toLowerCase().contains(val)) {
                            dataSize++;
                        }
                    }

                    //build data
                    data = new Object[dataSize][cols.length];
                    int r = 0;
                    int c = 0;
                    for (List<Object> d : selectQueryElements.getTable().getData()) {
                        if (d.get(selectQueryElements.getCondition().getCol().getIndex()).toString().toLowerCase().contains(val)) {
                            for (Column column : selectQueryElements.getCols()) {
                                data[r][c] = d.get(column.getIndex());
                                c++;
                            }
                            r++;
                            c = 0;
                        }
                    }
                } else if (selectQueryElements.getCondition().getValue().startsWith("'%")) {
                    //build data size
                    int dataSize = 0;
                    String val = selectQueryElements.getCondition().getValue().split("%")[1].split("'")[0];
                    for (List<Object> d : selectQueryElements.getTable().getData()) {
                        if (d.get(selectQueryElements.getCondition().getCol().getIndex()).toString().toLowerCase().endsWith(val)) {
                            dataSize++;
                        }
                    }

                    //build data
                    data = new Object[dataSize][cols.length];
                    int r = 0;
                    int c = 0;
                    for (List<Object> d : selectQueryElements.getTable().getData()) {
                        if (d.get(selectQueryElements.getCondition().getCol().getIndex()).toString().toLowerCase().endsWith(val)) {
                            for (Column column : selectQueryElements.getCols()) {
                                data[r][c] = d.get(column.getIndex());
                                c++;
                            }
                            r++;
                            c = 0;
                        }
                    }
                } else if (selectQueryElements.getCondition().getValue().endsWith("%'")) {
                    //build data size
                    int dataSize = 0;
                    String val = selectQueryElements.getCondition().getValue().split("%'")[0].split("'")[1];
                    for (List<Object> d : selectQueryElements.getTable().getData()) {
                        if (d.get(selectQueryElements.getCondition().getCol().getIndex()).toString().toLowerCase().startsWith(val)) {
                            dataSize++;
                        }
                    }
                    //build data
                    data = new Object[dataSize][cols.length];
                    int r = 0;
                    int c = 0;
                    for (List<Object> d : selectQueryElements.getTable().getData()) {
                        if (d.get(selectQueryElements.getCondition().getCol().getIndex()).toString().toLowerCase().startsWith(val)) {
                            for (Column column : selectQueryElements.getCols()) {
                                data[r][c] = d.get(column.getIndex());
                                c++;
                            }
                            r++;
                            c = 0;
                        }

                    }

                }
            } else {
                String val = selectQueryElements.getCondition().getValue();
                if (selectQueryElements.getCondition().getValue().startsWith("'")) {
                    //build data size
                    int dataSize = 0;
                    val = selectQueryElements.getCondition().getValue().split("'")[1].split("'")[0];
                    for (List<Object> d : selectQueryElements.getTable().getData()) {
                        if (d.get(selectQueryElements.getCondition().getCol().getIndex()).toString().equalsIgnoreCase(val)) {
                            dataSize++;
                        }
                    }

                    //build data
                    data = new Object[dataSize][cols.length];
                    int r = 0;
                    int c = 0;
                    for (List<Object> d : selectQueryElements.getTable().getData()) {
                        if (d.get(selectQueryElements.getCondition().getCol().getIndex()).toString().equalsIgnoreCase(val)) {
                            for (Column column : selectQueryElements.getCols()) {
                                data[r][c] = d.get(column.getIndex());
                                c++;
                            }
                            r++;
                            c = 0;
                        }

                    }

                } else {
                    //build data size
                    int dataSize = 0;
                    for (List<Object> d : selectQueryElements.getTable().getData()) {
                        if ((Double) d.get(selectQueryElements.getCondition().getCol().getIndex()) == Double.parseDouble(val)) {
                            dataSize++;
                        }
                    }

                    //build data
                    data = new Object[dataSize][cols.length];
                    int r = 0;
                    int c = 0;
                    for (List<Object> d : selectQueryElements.getTable().getData()) {
                        if ((Double) d.get(selectQueryElements.getCondition().getCol().getIndex()) == Double.parseDouble(val)) {
                            for (Column column : selectQueryElements.getCols()) {
                                data[r][c] = d.get(column.getIndex());
                                c++;
                            }
                            r++;
                            c = 0;
                        }

                    }
                }
            }
        } else {
            data = new Object[selectQueryElements.getTable().getData().size()][cols.length];
            int r = 0;
            int c = 0;
            for (List<Object> d : selectQueryElements.getTable().getData()) {
                for (Column column : selectQueryElements.getCols()) {
                    data[r][c] = d.get(column.getIndex());
                    c++;
                }
                r++;
                c = 0;
            }
        }
        PrettyPrint.print(cols, data);
        return new QueryResult(cols, data);
    }

}
