package org.nsdl.mptstore.query;

import java.util.List;

import org.nsdl.mptstore.core.ClosableIterator;
import org.nsdl.mptstore.rdf.Node;

/**
 * An iterator representing any number of query result rows.
 *
 * @author cwilper@cs.cornell.edu
 */
public interface QueryResults extends ClosableIterator<List<Node>> {

    /**
     * Get the column names for these query result rows.
     *
     * @return List<String> the ordered list of names.
     */
    public List<String> getTargets();

    /**
     * Get the next row of results.
     *
     * @throws RuntimeQueryException if there's an error getting the next row.
     */
    public List<Node> next();

    /**
     * Tell whether there's another row of results.
     *
     * @return true if there's another row, false if not.
     */
    public boolean hasNext();

    /**
     * Throws <code>UnsupportedOperationException</code>.
     *
     * This operation is not supported by <code>QueryResults</code>
     * implementations.
     *
     * @throws UnsupportedOperationException if this method is called.
     */
    public void remove() throws UnsupportedOperationException;

}
