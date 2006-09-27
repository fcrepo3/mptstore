package org.nsdl.mptstore.query;

import org.nsdl.mptstore.rdf.Node;

/** Represents a constraint on the value of a particular mapped node pattern
 *
 * @see org.nsdl.mptstore.rdf.NodeFilter
 *  
 * @author birkland
 *
 */
class MappableNodeFilter implements NodeFilter {
    private final MappableNodePattern<? extends Node> node;
    private final MappableNodePattern<? extends Node> value;
    private final String operator;

    /** Create a filter on a particular node pattern
     * 
     * @param node The node pattern to constrain
     * @param operator String representing some operator
     * @param value node pattern representing a constraint
     */
    public MappableNodeFilter(MappableNodePattern<? extends Node> node, String operator, 
            MappableNodePattern<? extends Node> constraint) {
        this.node = node;
        this.value = constraint;
        this.operator = operator;
    }
    
    public  MappableNodeFilter(NodeFilter filter) {
        this.node = new MappableNodePattern<Node>(filter.getNode());
        this.value = new MappableNodePattern<Node>(filter.getConstraint());
        this.operator = filter.getOperator();
    }
    
    public MappableNodePattern<? extends Node> getNode() {
        return node;
    }
    
    public MappableNodePattern<? extends Node> getConstraint() {
        return value;
    }
    
    public String getOperator() {
        return operator;
    }
    
    public String toString() {
        return node + " " + operator + " " + value;
    }
}
