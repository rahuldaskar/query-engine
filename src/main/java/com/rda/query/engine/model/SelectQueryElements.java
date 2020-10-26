package com.rda.query.engine.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SelectQueryElements {
    Table table;
    Column[] cols;
    Condition condition;
}
