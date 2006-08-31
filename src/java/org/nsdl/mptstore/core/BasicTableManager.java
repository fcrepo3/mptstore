package org.nsdl.mptstore.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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

    private DataSource _dataSource;

    private DDLGenerator _ddlGenerator;

    private String _mapTable;

    private String _soTablePrefix;

    /** Map of predicates to tables */
    private Map<String,String> _map;

    /** Map of tables to predicates */
    private Map<String,String> _reverseMap;

    /**
     * Initialize the table manager, creating the map table
     * if it doesn't yet exist, and populating the in-memory
     * map with the content of the map table.
     */
    public BasicTableManager(DataSource dataSource,
                             DDLGenerator ddlGenerator,
                             String mapTable,
                             String soTablePrefix) throws SQLException {

        _dataSource = dataSource;
        _ddlGenerator = ddlGenerator;
        _mapTable = mapTable;
        _soTablePrefix = soTablePrefix;

        Connection conn = dataSource.getConnection();
        try {
            if (!mapTableExists(conn)) {
                executeDDL(conn,
                           _ddlGenerator.getCreateMapTableDDL(_mapTable).iterator());
            }
            loadMapTable(conn);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                // warn: can't close connection
            }
        }

    }

    private boolean mapTableExists(Connection conn) throws SQLException {

        ResultSet results = conn.getMetaData().getTables(null, null,
                                                         _mapTable, null);
        try {
            if (results.next()) {
                return true;
            }
        } finally {
            try {
                results.close();
            } catch (SQLException e) {
                // warn: can't close result set
            }
        }

        // above only works for some dbs,
        // so if false, do another check
        Statement st = conn.createStatement();
        try {
            try {
                results = null;
                results = st.executeQuery("SELECT COUNT(*) FROM " + _mapTable);
                return results.next();
            } catch (SQLException e) {
                return false;
            } finally {
                try {
                    if (results != null) results.close();
                } catch (SQLException e) {
                    // warn: can't close result set
                }
            }
        } finally {
            try {
                st.close();
            } catch (SQLException e) {
                // warn: can't close statement
            }
        }
    }

    private void loadMapTable(Connection conn) throws SQLException {
        _map = new HashMap();
        _reverseMap = new HashMap();
        Statement st = conn.createStatement();
        ResultSet results = null;
        try {
            results = st.executeQuery("SELECT pKey, p FROM " + _mapTable);
            while (results.next()) {
                String table = _soTablePrefix + results.getInt(1);
                String predicate = results.getString(2);
                _map.put(predicate, table);
                _reverseMap.put(table, predicate);
            }
        } finally {
            if (results != null) {
                try {
                    results.close();
                } catch (SQLException e) {
                    // warn: can't close result set
                }
            }
            try {
                st.close();
            } catch (SQLException e) {
                // warn: can't close statement
            }
        }
    }

    // Implements TableManager.getOrMapTableFor(String, Connection)
    public String getOrMapTableFor(String predicate) throws SQLException {
        String table = getTableFor(predicate);
        if (table != null) {
            return table;
        } else {
            Connection conn = _dataSource.getConnection();
            try {
                return mapTableFor(predicate, conn);
            } finally {
                try { 
                    conn.close(); 
                } catch (SQLException e) { 
                    // warn: unable to release/close connection
                }
            }
        }
    }

    private synchronized String mapTableFor(String predicate,
                             Connection conn)
            throws SQLException {

        // re-check map in case the predicate was added
        // while this thread was blocking
        String table = getTableFor(predicate);
        if (table != null) {
            return table;
        } else {
            int id = addPredicateToMapTable(predicate, conn);
            try {
                table = _soTablePrefix + id;
                executeDDL(conn,
                           _ddlGenerator.getCreateSOTableDDL(table).iterator());
            } catch (SQLException e) {
                try {
                    // since we're not on a transaction, we must manually
                    // back out the prior INSERT into the map table
                    deletePredicateFromMapTable(predicate, conn);
                } catch (SQLException e2) {
                    // warn: unable to clean up map table after failed CREATE TABLE
                }
                throw e;
            }
            synchronized (_map) {
                _map.put(predicate, table);
                _reverseMap.put(table, predicate);
            }
            return table;
        }
    }

    private int addPredicateToMapTable(String predicate, Connection conn) 
            throws SQLException {

        // add to map table
        PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO " + _mapTable + " (p) VALUES (?)");
        try {
            ps.setString(1, predicate);
            ps.execute();
        } finally {
            try {
                ps.close();
            } catch (SQLException e) {
                // warn: unable to close statement
            }
        }

        // select db-generated id
        ps = conn.prepareStatement(
                "SELECT pKey from " + _mapTable + " WHERE p = ?");
        try {
            ps.setString(1, predicate);
            ResultSet rs = ps.executeQuery();
            try {
                rs.next();
                return rs.getInt(1);
            } finally {
                try {
                    rs.close();
                } catch (SQLException e) {
                    // warn: unable to close resultset
                }
            }
        } finally {
            try {
                ps.close();
            } catch (SQLException e) {
                // warn: unable to close statement
            }
        }

    }

    private void deletePredicateFromMapTable(String predicate, Connection conn) 
            throws SQLException {
        PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM " + _mapTable + " WHERE p = ?");
        try {
            ps.setString(1, predicate);
            ps.execute();
        } finally {
            try {
                ps.close();
            } catch (SQLException e) {
                // warn: unable to close statement
            }
        }
    }


    /**
     * Execute the given DDL statements.
     */
    private void executeDDL(Connection conn,
                            Iterator<String> ddlIter) throws SQLException {
        Statement st = conn.createStatement();
        try {
            while (ddlIter.hasNext()) {
                st.executeUpdate(ddlIter.next());
            }
        } finally {
            try {
                st.close();
            } catch (SQLException e) {
                // warn: unable to close statement
            }
        }
    }

    public String getTableFor(String predicate) {
        synchronized (_map) {
            return _map.get(predicate);
        }
    }

    public String getPredicateFor(String table) {
        synchronized (_map) {
            return _reverseMap.get(table);
        }
    }

    public Set<String> getTables() {
        synchronized (_map) {
            return new HashSet(_reverseMap.keySet());
        }
    }

    public Set<String> getPredicates() {
        synchronized (_map) {
            return new HashSet(_map.keySet());
        }
    }

}
