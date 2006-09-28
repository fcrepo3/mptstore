package org.nsdl.mptstore.query.component;

import org.nsdl.mptstore.rdf.Node;

/** Represents a constraint on the value of a particular mapped node pattern
 *
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

    /** Create a filter on a particular node pattern
     * 
     * @param node The node pattern to constrain
     * @param operator String representing some operator
     * @param value node pattern representing a constraint
     */
    public MappableNodeFilter(MappableNodePattern<T> node, String operator, 
            MappableNodePattern<T> constraint) {
        this.node = node;
        this.value = constraint;
        this.operator = operator;
    }
    
    public  MappableNodeFilter(NodeFilter<T> filter) {
        this.node = new MappableNodePattern<T>(filter.getNode());
        this.value = new MappableNodePattern<T>(filter.getConstraint());
        this.operator = filter.getOperator();
    }
    
    public MappableNodePattern<T> getNode() {
        return node;
    }
    
    public MappableNodePattern<T> getConstraint() {
        return value;
    }
    
    public String getOperator() {
        return operator;
    }
    
    public String toString() {
        return node + " " + operator + " " + value;
    }
}
