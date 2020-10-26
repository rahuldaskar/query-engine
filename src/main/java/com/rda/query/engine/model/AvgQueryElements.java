package com.rda.query.engine.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AvgQueryElements {
    Table table;
    Column col;
    Condition condition;
}
