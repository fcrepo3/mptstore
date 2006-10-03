package org.nsdl.mptstore.query;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

import java.text.ParseException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.List;

import org.apache.log4j.Logger;

import org.nsdl.mptstore.query.provider.SQLProvider;
import org.nsdl.mptstore.rdf.Node;
import org.nsdl.mptstore.util.DBUtil;
import org.nsdl.mptstore.util.NTriplesUtil;

/**
 * RDF query results generated from a list of SQL statements.
 *
 * This class executes the given SQL in the order given, and provides
 * an RDF result row for each JDBC ResultSet row.
 *
 * @author cwilper@cs.cornell.edu
 */
public class SQLUnionQueryResults implements QueryResults {

    /**
     * The Logger for this class.
     */
    private static final Logger LOG = 
            Logger.getLogger(SQLUnionQueryResults.class.getName());

    /**
     * The database connection to use for the SQL queries.
     */
    private Connection _conn;

    /**
     * The JDBC fetchSize to use for each SQL query.
     */
    private int _fetchSize;

    /**
     * Provides the SQL and column names for the query.
     */
    private SQLProvider _sqlProvider;

    /**
     * The SQL queries to execute.
     */
    private Iterator<String> _queries;

    /**
     * Whether this instance has been closed.
     */
    private boolean _closed;

    /**
     * The current JDBC ResultSet.
     */
    private ResultSet _results;

    /**
     * The current JDBC Statement.
     */
    private Statement _statement;

    /**
     * The row to be returned by the next call to next().
     */
    private List<Node> _nextTuple;

    /**
     * Instantiate SQLUnionQueryResults to work with the given SQL on the 
     * given connection.
     *
     * @param conn the database connection to use.
     * @param fetchSize the JDBC fetchSize to use for each SQL query.
     * @param sqlProvider provides the SQL and column names for the query.
     * @throws QueryException if an unexpected error occurs starting the query.
     */
    public SQLUnionQueryResults(final Connection conn,
                                final int fetchSize,
                                final SQLProvider sqlProvider) 
            throws QueryException {

        _conn = conn;
        _fetchSize = fetchSize;
        _sqlProvider = sqlProvider;

        _closed = false;

        try {
            _queries = sqlProvider.getSQL().iterator();
            try {
                startNextQuery();
            } catch (SQLException e) {
                throw new QueryException("Error querying database", e);
            }
            readNextTuple();
        } catch (QueryException e) {
            close();
            throw e;
        }
    }

    /**
     * Set _nextTuple to the next tuple.  
     *
     * If there are no more tuples, proactively close and set 
     * <code>_nextTuple</code> to <code>null</code>.
     *
     * @throws QueryException if an unexpected error occurs.
     */
    private void readNextTuple() throws QueryException {

        try {
            while (_results != null && !_results.next()) {
                startNextQuery();
            }

            if (_results == null) {
                _nextTuple = null;
            } else {
                int tupleSize = _sqlProvider.getTargets().size();
                _nextTuple = new ArrayList<Node>(tupleSize);
                for (int i = 1; i <= tupleSize; i++) {
                    String nodeString = DBUtil.getLongString(_results, i);
                    try {
                        if (nodeString != null) {
                            _nextTuple.add(NTriplesUtil.parseNode(nodeString));
                        } else {
                            _nextTuple.add(null);
                        }
                    } catch (ParseException e) {
                        throw new QueryException("Error parsing RDF node ("
                                + nodeString + ") from database: " 
                                + e.getMessage(), e);
                    }
                }
            }
        } catch (SQLException e) {
            throw new QueryException("Error querying database", e);
        }
    }

    /**
     * Start the next SQL query, setting _results as appropriate.
     *
     * Any resources tied up by the prior query, if any, will be cleaned up.
     * If there are no more queries, _results will be set to <code>null</code>.
     *
     * @throws SQLException if there is a database error.
     */
    private void startNextQuery() throws SQLException {
        if (_queries.hasNext()) {
            if (_results != null) {
                // close previous query
                _results.close();
                _statement.close();
            }
            _statement = _conn.createStatement();
            _statement.setFetchSize(_fetchSize);
            String query = _queries.next();
            LOG.info("Executing query:\n" + query);
            _results = _statement.executeQuery(query);
        } else {
            LOG.info("Finished executing all queries");
            close(); // proactively close if no more queries
            _results = null;
        }
    }

    /** {@inheritDoc} */
    public List<String> getTargets() {
        return _sqlProvider.getTargets();
    }

    /** {@inheritDoc} */
    public boolean hasNext() {
        return _nextTuple != null;
    }

    /** {@inheritDoc} */
    public List<Node> next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        } else {
            List<Node> thisTuple = _nextTuple;
            try {
                readNextTuple();
                return thisTuple;
            } catch (QueryException e) {
                close(); // proactively close on error
                LOG.error(e);
                throw new RuntimeQueryException(e);
            }
        }
    }

    /** {@inheritDoc} */
    public void remove() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public void close() {
        if (!_closed) {
            if (_results != null) {
                try { 
                    _results.close(); 
                } catch (Exception e) { 
                    LOG.warn("Error closing result set", e);
                }
            }
            if (_statement != null) {
                try { 
                    _statement.close(); 
                } catch (Exception e) { 
                    LOG.warn("Error closing statement", e);
                }
            }
            try {
                if (!_conn.getAutoCommit()) {
                    _conn.setAutoCommit(true);
                }
            } catch (Exception e) { 
                LOG.warn("Error setting autocommit", e);
            }
            try { 
                _conn.close(); 
            } catch (Exception e) { 
                LOG.warn("Error closing/releasing connection", e);
            }
            _closed = true;
        }
    }

    /**
     * In the event of garbage collection, make sure close() has occurred.
     *
     * Note: This is intended to be called by the VM, not by client code.
     */
    public void finalize() {
        close();
    }

}
