package org.nsdl.mptstore.core;

/**
 * Signals an error while adding or removing triples.
 *
 * @author cwilper@cs.cornell.edu
 */
public class ModificationException extends Exception {

    /**
     * Construct a ModificationException with a detail message.
     *
     * @param message The detail message.
     */
    public ModificationException(final String message) {
        super(message);
    }

    /**
     * Construct a ModificationException with a detail message and
     * a cause.
     *
     * @param message The detail message.
     * @param cause The underlying cause.
     */
    public ModificationException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
