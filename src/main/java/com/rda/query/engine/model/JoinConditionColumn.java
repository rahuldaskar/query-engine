package com.rda.query.engine.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JoinConditionColumn {
    String name;
    int index;
    Table table;
}
