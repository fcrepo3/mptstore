package org.nsdl.mptstore.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import org.nsdl.mptstore.query.QueryException;
import org.nsdl.mptstore.query.QueryResults;
import org.nsdl.mptstore.query.SQLUnionQueryResults;
import org.nsdl.mptstore.query.lang.QueryCompiler;
import org.nsdl.mptstore.query.lang.QueryLanguage;
import org.nsdl.mptstore.query.lang.spo.SPOQueryCompiler;
import org.nsdl.mptstore.query.provider.SQLProvider;
import org.nsdl.mptstore.rdf.PredicateNode;
import org.nsdl.mptstore.rdf.Triple;
import org.nsdl.mptstore.util.DBUtil;

/**
 * A <code>DatabaseAdaptor</code> designed to work with any database.
 *
 * This implementation uses only a subset of standard SQL92 syntax and
 * thus should be compatible with most RDBMS.
 *
 * @author cwilper@cs.cornell.edu
 */
public class GenericDatabaseAdaptor implements DatabaseAdaptor {

    /**
     * Logger for this class.
     */
    private static final Logger LOG =
            Logger.getLogger(GenericDatabaseAdaptor.class.getName());

    /**
     * The <code>TableManager</code> used by this instance.
     */
    private TableManager _tableManager;

    /**
     * Map of <code>QueryCompiler</code>s to use for each supported language.
     */
    private Map<QueryLanguage, QueryCompiler> _compilerMap;

    /**
     * Get an instance supporting the built-in query languages.
     *
     * @param tableManager The <code>TableManager</code> this instance
     *        should use.
     * @param backslashIsEscape A database vendor-specific specific value
     *        indicating whether the backslash character in a string is
     *        considered to be an escape character.
     */
    public GenericDatabaseAdaptor(final TableManager tableManager,
                                  final boolean backslashIsEscape) {
        _tableManager = tableManager;
        _compilerMap = new HashMap<QueryLanguage, QueryCompiler>();
        _compilerMap.put(QueryLanguage.SPO,
                         new SPOQueryCompiler(_tableManager,
                                              backslashIsEscape));
    }

    /**
     * Get an instance supporting the specified query languages.
     *
     * @param tableManager The <code>TableManager</code> this instance
     *        should use.
     * @param compilerMap A map of query language to query compiler.
     */
    public GenericDatabaseAdaptor(
            final TableManager tableManager,
            final Map<QueryLanguage, QueryCompiler> compilerMap) {
        _tableManager = tableManager;
        _compilerMap = compilerMap;
    }

    /** {@inheritDoc} */
    public void addTriples(final Connection conn,
                           final Iterator<Triple> triples)
            throws ModificationException {
        LOG.debug("Started adding triples to database");
        updateTriples(conn, triples, false);
        LOG.debug("Finished adding triples to database");
    }

    /** {@inheritDoc} */
    public void deleteTriples(final Connection conn,
                              final Iterator<Triple> triples)
            throws ModificationException {
        LOG.debug("Started deleting triples from database");
        updateTriples(conn, triples, true);
        LOG.debug("Finished deleting triples from database");
    }

    /**
     * Execute the given update operation of the given triples.
     *
     * @param conn The connection to execute the update on.
     * @param triples The triples to add or delete.
     * @param delete Boolean indicating whether the operation is an add
     *        or delete.
     * @throws ModificationException if the operation fails for any reason.
     */
    private void updateTriples(final Connection conn,
                               final Iterator<Triple> triples,
                               final boolean delete)
            throws ModificationException {

        Map<PredicateNode, PreparedStatement> statements =
                new HashMap<PredicateNode, PreparedStatement>();

        try {
            while (triples.hasNext()) {

                Triple triple = triples.next();

                if (LOG.isDebugEnabled()) {
                    String prefix;
                    if (delete) {
                        prefix = "Deleting ";
                    } else {
                        prefix = "Adding ";
                    }
                    LOG.debug(prefix + triple.toString());
                }

                PredicateNode predicate = triple.getPredicate();

                PreparedStatement statement = statements.get(predicate);
                if (statement == null) {
                    String table = _tableManager.getOrMapTableFor(predicate);
                    String sql;
                    if (delete) {
                        sql = "DELETE FROM " + table
                                + " WHERE s = ? AND o = ?";
                    } else {
                        sql = "INSERT INTO " + table
                                + " (s, o) VALUES (?, ?)";
                    }
                    statement = conn.prepareStatement(sql);
                    statements.put(predicate, statement);
                }

                statement.setString(1, triple.getSubject().toString());
                statement.setString(2, triple.getObject().toString());
                statement.execute();
            }
        } catch (SQLException e) {
            throw new ModificationException("Database update failed", e);
        } finally {

            // close all statements we created for this update
            Iterator<PreparedStatement> iter = statements.values().iterator();
            while (iter.hasNext()) {
                PreparedStatement statement = iter.next();
                try {
                    statement.close();
                } catch (SQLException e) {
                    LOG.warn("unable to close statement", e);
                }
            }
        }
    }

    /** {@inheritDoc} */
    public void deleteAllTriples(final Connection conn)
            throws ModificationException {
        try {
            _tableManager.dropAllPredicateTables();
        } catch (SQLException e) {
            throw new ModificationException("Failed to delete all triples", e);
        }
    }


    /** {@inheritDoc} */
    public QueryResults query(final Connection connection,
                              final QueryLanguage language,
                              final int fetchSize,
                              final boolean autoReleaseConnection,
                              final String query)
            throws QueryException {
        QueryResults results = null;
        try {
            QueryCompiler compiler = _compilerMap.get(language);
            if (compiler != null) {
                SQLProvider provider = compiler.compile(query);
                results = new SQLUnionQueryResults(connection,
                                                   provider,
                                                   fetchSize,
                                                   autoReleaseConnection);
                return results;
            } else {
                throw new QueryException("Query language not supported: "
                        + language.getName());
            }
        } finally {
            if (results == null && autoReleaseConnection) {
                DBUtil.release(connection);
            }
        }
    }

}
