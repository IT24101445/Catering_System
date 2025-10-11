package com.example.catering_system.delivery.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/debug")
public class DebugController {

    private final DataSource dataSource;

    public DebugController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/table-structure")
    public ResponseEntity<?> getTableStructure() {
        try {
            Map<String, Object> result = new HashMap<>();
            
            try (Connection connection = dataSource.getConnection()) {
                DatabaseMetaData metaData = connection.getMetaData();
                
                // Get drivers table structure
                List<Map<String, Object>> driversColumns = new ArrayList<>();
                try (ResultSet columns = metaData.getColumns(null, null, "drivers", null)) {
                    while (columns.next()) {
                        Map<String, Object> column = new HashMap<>();
                        column.put("name", columns.getString("COLUMN_NAME"));
                        column.put("type", columns.getString("TYPE_NAME"));
                        column.put("size", columns.getInt("COLUMN_SIZE"));
                        column.put("nullable", columns.getString("IS_NULLABLE"));
                        driversColumns.add(column);
                    }
                }
                
                result.put("drivers_table_columns", driversColumns);
                result.put("status", "SUCCESS");
                
            } catch (Exception e) {
                result.put("status", "ERROR");
                result.put("error", e.getMessage());
            }
            
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", "ERROR");
            error.put("error", e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    @PostMapping("/fix-drivers-table")
    public ResponseEntity<?> fixDriversTable() {
        try {
            Map<String, Object> result = new HashMap<>();
            
            try (Connection connection = dataSource.getConnection()) {
                // Check if password column exists and rename it
                try (ResultSet columns = connection.getMetaData().getColumns(null, null, "drivers", "password")) {
                    if (columns.next()) {
                        // Rename password to password_hash
                        connection.createStatement().execute("EXEC sp_rename 'drivers.password', 'password_hash', 'COLUMN'");
                        result.put("action", "Renamed password column to password_hash");
                    }
                }
                
                // Check if status column exists, if not add it
                try (ResultSet columns = connection.getMetaData().getColumns(null, null, "drivers", "status")) {
                    if (!columns.next()) {
                        connection.createStatement().execute("ALTER TABLE drivers ADD status VARCHAR(40) NOT NULL DEFAULT 'AVAILABLE'");
                        result.put("action", "Added status column");
                    }
                }
                
                result.put("status", "SUCCESS");
                result.put("message", "Table structure fixed");
                
            } catch (Exception e) {
                result.put("status", "ERROR");
                result.put("error", e.getMessage());
            }
            
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", "ERROR");
            error.put("error", e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }
}
