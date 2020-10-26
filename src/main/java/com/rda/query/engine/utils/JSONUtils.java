package com.rda.query.engine.utils;

import com.google.gson.Gson;
import com.rda.query.engine.model.Table;

public class JSONUtils {
    public static Table getTableFromJSON(String json) {
        Gson g = new Gson();
        return g.fromJson(json, Table.class);
    }
}
