package com.rda.query.engine.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderBy {
    Column sortCol;
    boolean asc;
}
