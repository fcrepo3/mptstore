package org.nsdl.mptstore.query.component;

import java.util.ArrayList;
import java.util.List;

import org.nsdl.mptstore.rdf.Node;

/** A set of concrete triple patterns and value constraints defining an
 *  RDF subgraph.
 * <p>
 * Triple patterns and filters ({@link NodeFilter} constraints on triple
 * value ranges) are as necessary to build a graph pattern
 * </p>
 * @author birkland
 *
 */
public class GraphPattern implements QueryElement {
    private ArrayList<TriplePattern> steps = new ArrayList<TriplePattern>();
    private ArrayList<NodeFilter<Node>> filters =
            new ArrayList<NodeFilter<Node>>();


    /** {@inheritDoc} */
    public QueryElement.Type getType() {
        return QueryElement.Type.GraphPattern;
    }

    /**
     * Get the list of patterns.
     *
     * @return the triple patterns.
     */
    public List<TriplePattern> getTriplePatterns() {
        return new ArrayList<TriplePattern>(steps);
    }

    /**
     * Get the list of filters.
     *
     * @return the node filters.
     */
    public List<NodeFilter<Node>> getFilters() {
        return new ArrayList<NodeFilter<Node>>(filters);
    }

    /**
     * Add a triple pattern to the list of patterns.
     *
     * @param s the triple pattern to add.
     */
    public void addTriplePattern(final TriplePattern s) {
        steps.add(s);
    }

    /**
     * Remove the given triple pattern from the list of patterns.
     *
     * @param s the triple pattern to remove.
     */
    public void remove(final TriplePattern s) {
        steps.remove(s);
    }

    /**
     * Add the given node filter to the list of filters.
     *
     * @param filter the node filter to add.
     */
    public void addFilter(final NodeFilter<Node> filter) {
        filters.add(filter);
    }

    /**
     * Remove the given node filter from the list of filters.
     *
     * @param filter the node filter to remove.
     */
    public void removeFilter(final String filter) {
        filters.remove(filter);
    }

    /** {@inheritDoc} */
     public String toString() {
        StringBuilder outString = new StringBuilder();
        for (TriplePattern step : steps) {
            outString.append(step + " .\n");
        }

        return outString.toString();
    }
}
