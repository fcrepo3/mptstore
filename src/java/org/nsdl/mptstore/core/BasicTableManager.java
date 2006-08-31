package org.nsdl.mptstore.core;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

/**
 * This implementation of TableManager creates predicate tables
 * and map entries as needed, using a separate connection provided
 * from a JDBC DataSource.
 *
 * It never attempts to execute DDL on the same transaction as DML.
 */
public class BasicTableManager implements TableManager {

    private Map<String,String> _map;

    /**
     * Initialize the table manager, creating the map table
     * if it doesn't yet exist, and populating the in-memory
     * map with the content of the map table.
     */
    public BasicTableManager(DataSource dataSource,
                             DDLGenerator ddlGenerator,
                             String mapTable,
                             String soTablePrefix) throws SQLException {

    }

    // Implements TableManager.getOrMapTableFor(String, Connection)
    public String getOrMapTableFor(String predicate,
                                   Connection conn) throws SQLException {
        String table = getTableFor(predicate);
        if (table != null) {
            return table;
        } else {
            int id = addPredicateToMapTable(predicate, conn);
            table = addPredicateTable(id, conn);
            _map.put(predicate, table);
            return table;
        }
    }

    private int addPredicateToMapTable(String predicate, Connection conn) throws SQLException {
        return 0;
    }

    private String addPredicateTable(int id, Connection conn) throws SQLException {
        return null;
    }

    public String getTableFor(String predicate) {
        return null;
    }

    public String getPredicateFor(String table) {
        return null;
    }

    public Set<String> getTables() {
        return null;
    }

    public Set<String> getPredicates() {
        return null;
    }

}
