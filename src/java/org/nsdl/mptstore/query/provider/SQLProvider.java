package org.nsdl.mptstore.query.provider;

import java.util.List;

import org.nsdl.mptstore.query.QueryException;

/**
 * Provides a list of <code>SELECT</code> statements and targets 
 * (column names) for RDF query.
 *
 * @author cwilper@cs.cornell.edu
 */
public interface SQLProvider {

    /**
     * Get the names of the values that are being selected.
     *
     * These should be in the same order specified in the original RDF
     * query, and should match the order of the associated values in the
     * SQL.
     *
     * @return the target names.
     */
    List<String> getTargets();

    /**
     * Get the sequence of <code>SELECT</code> statements.
     *
     * @return the <code>SELECT</code> statements.
     * @throws QueryException if the SQL could not be generated for any reason.
     */
    List<String> getSQL() throws QueryException;

}
