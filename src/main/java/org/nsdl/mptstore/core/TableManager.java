package org.nsdl.mptstore.core;

import java.sql.SQLException;

import java.util.Set;

import org.nsdl.mptstore.rdf.PredicateNode;

/**
 * Provides thread-safe read/write access to the tables used by MPTStore.
 *
 * Implementations of this interface will typically cache the predicate-to-table
 * mappings in memory, and will always persist them to the database.
 *
 * @author cwilper@cs.cornell.edu
 */
public interface TableManager {

    /**
     * Get the name of the table reserved for relationships of the given type.
     * <p>
     *   If such a table does not yet exist, it will be automatically created
     *   and permanently associated with the given predicate.
     * </p>
     *
     * @param predicate The type of relationship.
     * @return The table name for the given predicate, never <code>null</code>.
     * @throws SQLException if a database error occurs while the table is
     */
    String getOrMapTableFor(PredicateNode predicate) throws SQLException;

    /**
     * Get the name of the table reserved for relationships of the given type,
     * if it exists.
     *
     * @param predicate The type of relationship.
     * @return The table name for the given predicate, or <code>null</code>
     *         if no such predicate exists in the graph.
     */
    String getTableFor(PredicateNode predicate);

    /**
     * Get the predicate that's mapped to the given table, if such a mapping
     * exists.
     *
     * @param table The name of the table.
     * @return The predicate, or <code>null</code> if no such mapping exists.
     */
    PredicateNode getPredicateFor(String table);

    /**
     * Get the set of tables that store per-predicate relationships.
     *
     * @return A set with zero or more table names.
     */
    Set<String> getTables();

    /**
     * Get the set of predicates for which a table mapping exists.
     *
     * @return A set with zero or more predicates.
     */
    Set<PredicateNode> getPredicates();

    /**
     * Drop all unused predicate tables and mappings.
     *
     * @return the number of dropped predicate tables.
     * @throws SQLException if a database error occured during the operation.
     */
    int dropEmptyPredicateTables() throws SQLException;

    /**
     * Drop all predicate tables and mappings, effectively
     * re-initializing the triplestore.
     *
     * @return the number of dropped predicate tables.
     * @throws SQLException if a database error occured during the operation.
     */
    int dropAllPredicateTables() throws SQLException;

}
