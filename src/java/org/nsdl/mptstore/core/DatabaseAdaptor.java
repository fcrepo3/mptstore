package org.nsdl.mptstore.core;

import java.sql.Connection;

import java.util.Iterator;

import org.nsdl.mptstore.query.QueryException;
import org.nsdl.mptstore.query.QueryResults;
import org.nsdl.mptstore.query.lang.QueryLanguage;
import org.nsdl.mptstore.rdf.Triple;

/**
 * The main high-level interface for working with a triplestore.
 *
 * <p>
 *   With a <code>DatabaseAdaptor</code>, you can add and delete triples
 *   and perform interpreted queries against the triplestore.
 * </p>
 * <p>
 *   Note that each method takes an existing JDBC <code>Connection</code> as a 
 *   parameter.  Client applications are expected to open/close or 
 *   borrow/release connections as needed.  In addition, transaction
 *   boundaries are expected to be managed externally.
 * </p>
 *
 * @author cwilper@cs.cornell.edu
 */
public interface DatabaseAdaptor {

    /**
     * Add the given triples.
     *
     * @param conn The database connection to use.
     * @param triples The triples to add.
     * @throws ModificationException if the operation failed for any reason.
     */
    public void addTriples(Connection conn,
                           Iterator<Triple> triples) 
            throws ModificationException;

    /**
     * Delete the given triples.
     *
     * @param conn The database connection to use.
     * @param triples The triples to delete.
     * @throws ModificationException if the operation failed for any reason.
     */
    public void deleteTriples(Connection conn, 
                              Iterator<Triple> triples) 
            throws ModificationException;

    /**
     * Delete all triples.
     *
     * @param conn The database connection to use.
     * @throws ModificationException if the operation failed for any reason.
     */
    public void deleteAllTriples(Connection conn)
            throws ModificationException;

    /**
     * Evaluate the given query in the specified language
     * and return the results.
     *
     * <p>
     *   NOTE: When all query results have been exhausted, or if the query 
     *   fails, the connection will be automatically closed/released.
     * </p>
     *
     * @param conn The database connection to use.
     * @param lang The language of the query.
     * @param fetchSize The number of results to request from the database
     *                  at one time.
     * @param queryText The query.
     * @throws QueryException if the query failed for any reason.
     */
    public QueryResults query(Connection conn, 
                              QueryLanguage lang,
                              int fetchSize,
                              String queryText) 
            throws QueryException;

}
