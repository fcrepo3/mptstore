package org.nsdl.mptstore.query.component;

import org.nsdl.mptstore.rdf.Node;

/**
 * Represents a constraint on the value of a particular mapped node pattern.
 *
 * @param <T> The type of node that is being constrained by this filter.
 *            Typically, this is one of SubjectNode, PredicateNode,
 *            ObjectNode, or just Node if the exact node type is unimportant
 *            or unknown.
 *
 * @author birkland
 */
public class MappableNodeFilter<T extends Node> implements NodeFilter<T> {
    private final MappableNodePattern<T> theNode;
    private final MappableNodePattern<T> theValue;
    private final String theOperator;

    /** Create a filter on a particular node pattern.
     *
     * @param node The node pattern to constrain
     * @param operator String representing some operator
     * @param constraint node pattern representing a constraint
     */
    public MappableNodeFilter(final MappableNodePattern<T> node,
                              final String operator,
                              final MappableNodePattern<T> constraint) {
        theNode = node;
        theValue = constraint;
        theOperator = operator;
    }

    /**
     * Create a mappable node filter from an existing node filter.
     *
     * @param filter the existing filter.
     */
    public MappableNodeFilter(final NodeFilter<T> filter) {
        theNode = new MappableNodePattern<T>(filter.getNode());
        theValue = new MappableNodePattern<T>(filter.getConstraint());
        theOperator = filter.getOperator();
    }

    /** {@inheritDoc} */
    public MappableNodePattern<T> getNode() {
        return theNode;
    }

    /** {@inheritDoc} */
    public MappableNodePattern<T> getConstraint() {
        return theValue;
    }

    /** {@inheritDoc} */
    public String getOperator() {
        return theOperator;
    }

    /** {@inheritDoc} */
    public String toString() {
        return theNode + " " + theOperator + " " + theValue;
    }
}
