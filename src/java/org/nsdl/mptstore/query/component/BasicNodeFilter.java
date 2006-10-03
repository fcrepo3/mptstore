package org.nsdl.mptstore.query.component;

import org.nsdl.mptstore.rdf.Node;

/** 
 * Generic implementation of a {@link NodeFilter}.
 * 
 * @param <T> The type of node that is being constrained by this filter.  
 *            Typically, this is one of SubjectNode, PredicateNode, ObjectNode, 
 *            or just Node if the exact node type is unimportant or unknown.
 * 
 * @author birkland
 */
public class BasicNodeFilter<T extends Node> implements NodeFilter<T> {
    private final NodePattern<T> theNode;
    private final NodePattern<T> theConstraint;
    private final String theOperator;

    /** Create a filter on a particular node pattern.
     * 
     * @param node The node pattern to constrain
     * @param operator String representing some operator
     * @param constraint node pattern representing a constraint
     */
    public BasicNodeFilter(final NodePattern<T> node, 
                           final String operator, 
                           final NodePattern<T> constraint) {
        theNode = node;
        theConstraint = constraint;
        theOperator = operator;
    }

    /** {@inheritDoc} */
    public NodePattern<T> getNode() {
        return theNode;
    }
    
    /** {@inheritDoc} */
    public NodePattern<T> getConstraint() {
        return theConstraint;
    }
    
    /** {@inheritDoc} */
    public String getOperator() {
        return theOperator;
    }

}
