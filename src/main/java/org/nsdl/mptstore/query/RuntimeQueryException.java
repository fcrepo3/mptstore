package org.nsdl.mptstore.query;

/**
 * Unchecked exception signaling a query-related error.
 * <p>
 *   This exception is thrown when a <code>QueryException</code> occurs
 *   in a context where a checked exception cannot be thrown due to
 *   interface restrictions.
 * </p>
 *
 * @author cwilper@cs.cornell.edu
 */
public class RuntimeQueryException extends RuntimeException {

    /**
     * Construct a RuntimeQueryException representing the given QueryException.
     *
     * @param cause the cause, which is always a <code>QueryException</code>.
     */
    public RuntimeQueryException(final QueryException cause) {
        super(cause);
    }

}
