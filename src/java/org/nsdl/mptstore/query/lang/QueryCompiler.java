package org.nsdl.mptstore.query.lang;

import org.nsdl.mptstore.query.QueryException;
import org.nsdl.mptstore.query.provider.SQLProvider;

/**
 * Converts an RDF query in some language to one or more SQL queries.
 *
 * @author cwilper@cs.cornell.edu
 */
public interface QueryCompiler {

    /**
     * Compile the given query.
     *
     * @param queryText the RDF query.
     * @throws QueryException if there's an error querying.
     * @return SQLProvider provides the SQL and result column names.
     */
    public SQLProvider compile(String queryText) throws QueryException;

}
