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
    void addTriples(Connection conn, Iterator<Triple> triples)
            throws ModificationException;

    /**
     * Delete the given triples.
     *
     * @param conn The database connection to use.
     * @param triples The triples to delete.
     * @throws ModificationException if the operation failed for any reason.
     */
    void deleteTriples(Connection conn, Iterator<Triple> triples)
            throws ModificationException;

    /**
     * Delete all triples.
     *
     * @param conn The database connection to use.
     * @throws ModificationException if the operation failed for any reason.
     */
    void deleteAllTriples(Connection conn)
            throws ModificationException;

    /**
     * Evaluate the given query in the specified language and return
     * the results.
     *
     * <h2>Who releases the connection?</h2>
     * <p>
     *   That depends.  If <code>autoReleaseConnection</code> is
     *   <code>true</code>, the caller is guaranteed that if the query
     *   fails or the results are closed at any time, the connection
     *   will be restored to auto-commit mode and released.  This
     *   can greatly simplify the caller's job as it can forget about
     *   the connection and just make sure the <code>QueryResults</code>
     *   object is closed.
     * </p>
     * <p>
     *   On the other hand, if <code>autoReleaseConnection</code>
     *   is <code>false</code>, the caller is entirely responsible
     *   for the connection.  This is useful in cases where the
     *   query is only part of an as-yet incomplete transaction.
     * </p>
     *
     * <h2>How do I avoid running out of memory?</h2>
     * <p>
     *   The <code>fetchSize</code> parameter can be used to avoid
     *   memory exhaustion when the number of expected results may be
     *   very large.  The parameter acts as a hint to the JDBC driver
     *   as to the number of rows that should be retrieved from the
     *   database at a time.
     * </p>
     * <p>
     *   If specified as <code>0</code>, the hint is ignored and the
     *   database will likely send all results at once for each
     *   <code>ResultSet</code>.  On very large result sets, this can
     *   result in memory exhaustion.
     * </p>
     * <p>
     *   If you don't want to accept the default behavior, you should
     *   also be aware of how the underlying RDBMS handles fetch sizes.
     *   For example:
     *   <ul>
     *     <li> As of MySQL 5.0, the only valid values for fetchSize are
     *          <code>0</code> and <code>Integer.MIN_VALUE</code>.
     *          The latter actually provides one result at a time
     *          and has table locking consequences.
     *     </li>
     *     <li> As of Postgres 8.1, any custom fetch size will work,
     *          but "implicit cursors" are not supported, so the query
     *          must occur within a transaction in order for the
     *          value to be respected.
     *     </li>
     *   </ul>
     * </p>
     *
     * @param connection the database connection to use.
     * @param lang the language of the query.
     * @param fetchSize gives the JDBC driver a hint as to the number of rows
     *                  that should be fetched from the database when more
     *                  rows are needed.  If zero, the hint is ignored and the
     *                  database may send all results to the driver at once.
     * @param autoReleaseConnection whether to automatically release/close
     *                  the connection if the query fails or the results
     *                  are closed.
     * @param queryText The query.
     * @return the results.
     * @throws QueryException if the query failed for any reason.
     */
    QueryResults query(Connection connection,
                       QueryLanguage lang,
                       int fetchSize,
                       boolean autoReleaseConnection,
                       String queryText)
            throws QueryException;

}
