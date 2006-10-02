package org.nsdl.mptstore.core;

import java.util.Iterator;

/**
 * An <code>Iterator</code> that should be closed when finished.
 *
 * @param <E> the type of object to be iterated.
 * @author cwilper@cs.cornell.edu
 */
public interface ClosableIterator<E> extends Iterator<E> {

    /**
     * Release any resources tied up by this iterator.
     */
    void close();

}
