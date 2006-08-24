package org.nsdl.mptstore.core;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.Set;

public class BasicTableManager implements TableManager {

    public BasicTableManager(Connection conn,
                             DDLGenerator ddlGenerator,
                             String mapTable,
                             String soTablePrefix) {
    }

    public String getOrCreateTableFor(String predicate,
                                      Connection conn) throws SQLException {
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
