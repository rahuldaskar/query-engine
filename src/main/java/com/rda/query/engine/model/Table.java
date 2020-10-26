package com.rda.query.engine.model;


/* Table jsonstructure
* {
      "Schema": "tablename",
      "Columns": [],
      "Types": [],
      "Data": []
    }
*
* */

import lombok.Data;

import java.util.List;

@Data
public class Table {
    String schema;
    List<String> columns;
    List<String> types;
    List<List<Object>> data;
}
