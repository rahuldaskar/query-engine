package com.rda.query.engine.processor;

import com.rda.query.engine.exceptions.OperatorNotSupportedException;
import com.rda.query.engine.model.AvgQueryElements;
import com.rda.query.engine.utils.PrettyPrint;

import java.util.List;

public class AvgQueryProcessor {
    //only supports like and = condition on single col
    //col like %abcd or col like abcd% or col like %abcd%
    //col = 'val'
    public static int process(AvgQueryElements avgQueryElements) {
        Double sum = 0d;
        int count = 0;
        if (avgQueryElements.getCondition() != null) {

            String operator = avgQueryElements.getCondition().getOperator();

            if (!avgQueryElements.getCondition().getOperator().equalsIgnoreCase("like") && !avgQueryElements.getCondition().getOperator().equals("=")) {
                throw new OperatorNotSupportedException("Only LIKE and = condition is supported for AVG function!");
            }

            if (operator.equalsIgnoreCase("like")) {
                if (avgQueryElements.getCondition().getValue().startsWith("'%") && avgQueryElements.getCondition().getValue().endsWith("%'")) {
                    String val = avgQueryElements.getCondition().getValue().split("%")[1];
                    for (List<Object> d : avgQueryElements.getTable().getData()) {
                        if (d.get(avgQueryElements.getCondition().getCol().getIndex()).toString().toLowerCase().contains(val)) {
                            if (!d.get(avgQueryElements.getCol().getIndex()).toString().trim().isEmpty()) {
                                sum = sum + (Double) d.get(avgQueryElements.getCol().getIndex());
                            }
                            count++;
                        }
                    }
                } else if (avgQueryElements.getCondition().getValue().startsWith("'%")) {
                    String val = avgQueryElements.getCondition().getValue().split("%")[1].split("'")[0];
                    for (List<Object> d : avgQueryElements.getTable().getData()) {
                        if (d.get(avgQueryElements.getCondition().getCol().getIndex()).toString().toLowerCase().endsWith(val)) {
                            if (!d.get(avgQueryElements.getCol().getIndex()).toString().trim().isEmpty()) {
                                sum = sum + (Double) d.get(avgQueryElements.getCol().getIndex());
                            }
                            count++;
                        }
                    }
                } else if (avgQueryElements.getCondition().getValue().endsWith("%'")) {
                    String val = avgQueryElements.getCondition().getValue().split("%'")[0].split("'")[1];
                    for (List<Object> d : avgQueryElements.getTable().getData()) {
                        if (d.get(avgQueryElements.getCondition().getCol().getIndex()).toString().toLowerCase().startsWith(val)) {
                            if (!d.get(avgQueryElements.getCol().getIndex()).toString().trim().isEmpty()) {
                                sum = sum + (Double) d.get(avgQueryElements.getCol().getIndex());
                            }
                            count++;
                        }
                    }
                }
            } else {
                String val = avgQueryElements.getCondition().getValue();
                if (avgQueryElements.getCondition().getValue().startsWith("'")) {
                    val = avgQueryElements.getCondition().getValue().split("'")[1].split("'")[0];
                    for (List<Object> d : avgQueryElements.getTable().getData()) {
                        if (d.get(avgQueryElements.getCondition().getCol().getIndex()).toString().equalsIgnoreCase(val)) {
                            if (!d.get(avgQueryElements.getCol().getIndex()).toString().trim().isEmpty()) {
                                sum = sum + (Double) d.get(avgQueryElements.getCol().getIndex());
                            }
                            count++;
                        }
                    }
                } else {
                    for (List<Object> d : avgQueryElements.getTable().getData()) {
                        if ((Double) d.get(avgQueryElements.getCondition().getCol().getIndex()) == Double.parseDouble(val)) {
                            if (!d.get(avgQueryElements.getCol().getIndex()).toString().trim().isEmpty()) {
                                sum = sum + (Double) d.get(avgQueryElements.getCol().getIndex());
                            }
                            count++;
                        }
                    }
                }
            }
        } else {
            for (List<Object> d : avgQueryElements.getTable().getData()) {
                if (!d.get(avgQueryElements.getCol().getIndex()).toString().trim().isEmpty()) {
                    sum = sum + (Double) d.get(avgQueryElements.getCol().getIndex());
                }
                count++;
            }
        }
        int avg = (int) (sum / count);
        Object[][] data = new Object[1][1];
        data[0][0] = avg;
        String[] cols = new String[]{"avg(" + avgQueryElements.getCol().getName() + ")"};
        PrettyPrint.print(cols, data);
        return avg;
    }
}
