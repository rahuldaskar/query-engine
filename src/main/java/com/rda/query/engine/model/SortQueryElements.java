package com.rda.query.engine.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SortQueryElements {
    Table table;
    Column[] cols;
    OrderBy orderBy;
}
