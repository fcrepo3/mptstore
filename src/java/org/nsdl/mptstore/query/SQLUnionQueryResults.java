package org.nsdl.mptstore.query;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.List;

import org.nsdl.mptstore.util.DBUtil;

/**
 * Query results that wrap a set of SQL database queries,
 * run back-to-back on a single connection.
 */
public class SQLUnionQueryResults implements QueryResults {

    private Connection _conn;
    private int _fetchSize;
    private SQLProvider _sqlProvider;

    private Iterator<String> _queries;

    private boolean _closed;

    private ResultSet _results;
    private Statement _statement;

    private List<String> _nextTuple;

    public SQLUnionQueryResults(Connection conn,
                                int fetchSize,
                                SQLProvider sqlProvider) 
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
     * Set _nextTuple to the next tuple,
     * If there are no more tuples, proactively close
     * and set _nextTuple to null.
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
                _nextTuple = new ArrayList<String>(tupleSize);
                for (int i = 1; i <= tupleSize; i++) {
                    _nextTuple.add(DBUtil.getLongString(_results, i));
                }
            }
        } catch (SQLException e) {
            throw new QueryException("Error querying database", e);
        }
    }

    private void startNextQuery() throws SQLException {
        if (_queries.hasNext()) {
            if (_results != null) {
                // close previous query
                _results.close();
                _statement.close();
            }
            _statement = _conn.createStatement();
            _statement.setFetchSize(_fetchSize);
            _results = _statement.executeQuery(_queries.next());
        } else {
            close(); // proactively close if no more queries
            _results = null;
        }
    }

    // Implements QueryResults.getTargets()
    public List<String> getTargets() {
        return _sqlProvider.getTargets();
    }

    // Implements Iterator.hasNext()
    public boolean hasNext() {
        return _nextTuple != null;
    }

    // Implements Iterator.next()
    public List<String> next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        } else {
            List<String> thisTuple = _nextTuple;
            try {
                readNextTuple();
                return thisTuple;
            } catch (QueryException e) {
                close(); // proactively close on error
                throw new RuntimeQueryException(e);
            }
        }
    }

    // Implements Iterator.remove()
    public void remove() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    // Implements ClosableIterator.close()
    public void close() {
        if (!_closed) {
            if (_results != null) {
                try { _results.close(); } catch (Exception e) { }
            }
            if (_statement != null) {
                try { _statement.close(); } catch (Exception e) { }
            }
            try {
                if (!_conn.getAutoCommit()) {
                    _conn.setAutoCommit(true);
                }
            } catch (Exception e) { }
            try { _conn.close(); } catch (Exception e) { }
            _closed = true;
        }
    }

    /**
     * In the event of garbage collection, make sure close()
     * has occurred.
     */
    public void finalize() {
        close();
    }

}
