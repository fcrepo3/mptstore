package org.nsdl.mptstore.query;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.List;

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
            if (startNextQuery()) {
                readNextTuple();
            }
        } catch (QueryException e) {
            close();
            throw e;
        }
    }

    private boolean startNextQuery() {
        // todo: cleanup from previous here?
        return true; // if the next query was started
    }

    /**
     * Set _nextTuple to the next tuple,
     * or <code>null</code> if no more results.
     */
    private void readNextTuple() throws QueryException {

        if (_results != null) {

        
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
        if (_closed) {
            throw new NoSuchElementException();
        } else {
            List<String> thisTuple = _nextTuple;
            try {
                readNextTuple();
                return thisTuple;
            } catch (QueryException e) {
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

    public void finalize() {
        close();
    }

}
