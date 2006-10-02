package org.nsdl.mptstore.query.component;

import org.nsdl.mptstore.rdf.Node;

/** Represents a constraint on the value of a particular mapped node pattern.
 *
 * @param <T> The type of node that is being constrained by this filter.  Typically,
 * this is one of SubjectNode, PredicateNode, ObjectNode, or just Node if the
 * exact node type is unimportant or unknown.
 *   
 * @author birkland
 *
 */
public class MappableNodeFilter<T extends Node> implements NodeFilter<T> {
    private final MappableNodePattern<T> node;
    private final MappableNodePattern<T> value;
    private final String operator;

    /** Create a filter on a particular node pattern.
     * 
     * @param node The node pattern to constrain
     * @param operator String representing some operator
     * @param constraint node pattern representing a constraint
     */
    public MappableNodeFilter(MappableNodePattern<T> node, String operator, 
            MappableNodePattern<T> constraint) {
        this.node = node;
        this.value = constraint;
        this.operator = operator;
    }

    /**
     * Create a mappable node filter from an existing node filter.
     *
     * @param filter the existing filter.
     */
    public  MappableNodeFilter(NodeFilter<T> filter) {
        this.node = new MappableNodePattern<T>(filter.getNode());
        this.value = new MappableNodePattern<T>(filter.getConstraint());
        this.operator = filter.getOperator();
    }

    /** {@inheritDoc} */
    public MappableNodePattern<T> getNode() {
        return node;
    }
    
    /** {@inheritDoc} */
    public MappableNodePattern<T> getConstraint() {
        return value;
    }
    
    /** {@inheritDoc} */
    public String getOperator() {
        return operator;
    }
    
    /** {@inheritDoc} */
    public String toString() {
        return node + " " + operator + " " + value;
    }
}
