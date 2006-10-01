package org.nsdl.mptstore.query.lang;

import org.nsdl.mptstore.query.QueryException;

/**
 * A <code>QueryException</code> that signals a malformed RDF query.
 *
 * @author cwilper@cs.cornell.edu
 */
public class QuerySyntaxException extends QueryException {

    /**
     * Construct a QueryException with a detail message.
     *
     * @param message the detail message.
     */
    public QuerySyntaxException(String message) {
        super(message);
    }

    /**
     * Construct a QueryException with a detail message and a cause.
     *
     * @param message the detail message.
     * @param cause the cause.
     */
    public QuerySyntaxException(String message, Throwable cause) {
        super(message, cause);
    }

}
