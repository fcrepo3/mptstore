package org.nsdl.mptstore.core;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.Map;
import java.util.Set;

/**
 * This implementation of TableManager creates predicate tables
 * as needed.  It assumes that DDL can occur in the same
 * transaction as updates.
 *
 * This is true for Postgres, but may not be true for other
 * databases.  An alternate implementation might proactively
 * create tables on a different connection in the background.
 */
public class BasicTableManager implements TableManager {

    private Map<String,String> _map;

    /**
     * Initialize the table manager, creating the map table
     * if it doesn't yet exist, and populating the in-memory
     * map with the content of the map table.
     */
    public BasicTableManager(Connection conn,
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
