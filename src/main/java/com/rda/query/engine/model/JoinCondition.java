package com.rda.query.engine.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JoinCondition {
    JoinConditionColumn col;
    String operator;
    String value;
}
