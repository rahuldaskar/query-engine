package com.rda.query.engine.utils;

import dnl.utils.text.table.TextTable;

public class PrettyPrint {
    public static void print(String[] cols, Object[][] data) {
        TextTable tt = new TextTable(cols, data);
        tt.printTable();
    }
}
