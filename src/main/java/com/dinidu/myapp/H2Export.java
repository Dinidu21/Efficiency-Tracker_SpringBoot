package com.dinidu.myapp;

import java.sql.Connection;
import java.sql.DriverManager;

public class H2Export {
    public static void main(String[] args) throws Exception {
        String jdbcUrl = "jdbc:h2:file:./data/efficiency_tracker";
        try (Connection conn = DriverManager.getConnection(jdbcUrl, "sa", "")) {
            conn.createStatement().execute("SCRIPT TO 'h2-export.sql'");
        }
        System.out.println("Export complete.");
    }
}
