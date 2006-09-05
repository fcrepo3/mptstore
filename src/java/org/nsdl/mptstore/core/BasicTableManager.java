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

import org.apache.log4j.Logger;

/**
 * This implementation of TableManager creates predicate tables
 * and map entries as needed, using a separate connection provided
 * from a JDBC DataSource.
 *
 * It never attempts to execute DDL on the same transaction as DML.
 */
public class BasicTableManager implements TableManager {

    private static final Logger _LOG = Logger.getLogger(BasicTableManager.class.getName());

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
                _LOG.info("Creating map table");
                executeDDL(conn,
                           _ddlGenerator.getCreateMapTableDDL(_mapTable).iterator());
            }
            loadMapTable(conn);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                _LOG.warn("unable to close/release connection", e);
            }
        }

    }

    private boolean mapTableExists(Connection conn) throws SQLException {

        ResultSet results = conn.getMetaData().getTables(null, null,
                                                         _mapTable, null);
        try {
            if (results.next()) {
                _LOG.info("Found pre-existing map table");
                return true;
            }
        } finally {
            try {
                results.close();
            } catch (SQLException e) {
                _LOG.warn("unable to close result set", e);
            }
        }

        // above only works for some dbs,
        // so if false, do another check
        Statement st = conn.createStatement();
        try {
            boolean exists = false;
            try {
                results = null;
                results = st.executeQuery("SELECT COUNT(*) FROM " + _mapTable);
                exists = results.next();
            } catch (SQLException e) {
            } finally {
                try {
                    if (results != null) results.close();
                } catch (SQLException e) {
                    _LOG.warn("unable to close result set", e);
                }
            }
            if (exists) {
                _LOG.info("Found pre-existing map table");
            } else {
                _LOG.info("Map table does not yet exist");
            }
            return exists;
        } finally {
            try {
                st.close();
            } catch (SQLException e) {
                _LOG.warn("unable to close statement", e);
            }
        }
    }

    private void loadMapTable(Connection conn) throws SQLException {
        _LOG.info("Loading map table");
        _map = new HashMap<String,String>();
        _reverseMap = new HashMap<String,String>();
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
                    _LOG.warn("unable to close result set", e);
                }
            }
            try {
                st.close();
            } catch (SQLException e) {
                _LOG.warn("unable to close statement", e);
            }
        }
    }

    // Implements TableManager.getOrMapTableFor(String)
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
                    _LOG.warn("unable to close/release connection", e);
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
            _LOG.info("Mapping new table for predicate: " + predicate);
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
                    _LOG.warn("unable to clean up entry from map table after "
                            + "failure to create predicate table", e2);
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
                _LOG.warn("unable to close statement", e);
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
                    _LOG.warn("unable to close result set", e);
                }
            }
        } finally {
            try {
                ps.close();
            } catch (SQLException e) {
                _LOG.warn("unable to close statement", e);
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
                _LOG.warn("unable to close statement", e);
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
                String ddl = ddlIter.next();
                _LOG.info("Executing DDL: " + ddl);
                st.executeUpdate(ddl);
            }
        } finally {
            try {
                st.close();
            } catch (SQLException e) {
                _LOG.warn("unable to close statement", e);
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
            return new HashSet<String>(_reverseMap.keySet());
        }
    }

    public Set<String> getPredicates() {
        synchronized (_map) {
            return new HashSet<String>(_map.keySet());
        }
    }

    // Implements TableManager.dropEmptyPredicateTables()
    public int dropEmptyPredicateTables() throws SQLException {
        _LOG.info("Dropping empty predicate tables");
        return dropPredicateTables(false);
    }

    private synchronized int dropPredicateTables(boolean all) throws SQLException {

        int dropCount = 0;
        Connection conn = _dataSource.getConnection();
        try {
            Iterator<String> tables = getTables().iterator();
            while (tables.hasNext()) {
                String table = tables.next();
                if (all || isPredicateTableEmpty(table, conn)) {
                    String predicate = getPredicateFor(table);
                    unmapPredicate(predicate, table, conn);
                    dropCount++;
                }
            }
            return dropCount;
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                _LOG.warn("unable to close/release connection", e);
            }
        }
    }

    // Implements Tablemanager.dropAllPredicateTables()
    public int dropAllPredicateTables() throws SQLException {
        _LOG.info("Dropping all predicate tables");
        return dropPredicateTables(true);
    }

    private boolean isPredicateTableEmpty(String table, 
                                          Connection conn) throws SQLException {
        Statement st = conn.createStatement();
        try {
            ResultSet results = st.executeQuery("SELECT MAX(s) FROM " + table);
            try {
                return !results.next();
            } finally {
                try {
                    results.close();
                } catch (SQLException e) {
                    _LOG.warn("unable to close result set", e);
                }
            }
        } finally {
            try {
                st.close();
            } catch (SQLException e) {
                _LOG.warn("unable to close statement", e);
            }
        }
    }

    /**
     * Remove the predicate from the memory and database maps,
     * then drop the associated table.
     */
    private void unmapPredicate(String predicate,
                                String table,
                                Connection conn) 
            throws SQLException {

        _LOG.info("Unmapping " + predicate + " and dropping associated "
                + "table: " + table);

        _map.remove(predicate);
        _reverseMap.remove(table);

        PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM " + _mapTable + " WHERE p = ?");
        try {
            ps.setString(1, predicate);
            ps.execute();
        } finally {
            try {
                ps.close();
            } catch (SQLException e) {
                _LOG.warn("unable to close statement", e);
            }
        }

        executeDDL(conn,
                   _ddlGenerator.getDropSOTableDDL(table).iterator());
         
    }

}
