package com.rda.query.engine.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JoinQueryElements {
    JoinConditionColumn selectCol;
    JoinCondition[] joinConditions;
    Table table1;
    Table table2;
    Column c1;
    Column c2;

}
