package org.nsdl.mptstore.query.component;

import org.nsdl.mptstore.rdf.Node;

/** Generic  implementation of a {@link org.nsdl.mptstore.query.component.NodeFilter} 
 * 
 * 
 * @param <T> The type of node that is being constrained by this filter.  Typically,
 * this is one of SubjectNode, PredicateNode, ObjectNode, or just Node if the
 * exact node type is unimportant or unknown.
 * 
 * @author birkland
 */
public class BasicNodeFilter<T extends Node> implements NodeFilter<T> {
    private final NodePattern<T> node;
    private final NodePattern<T> constraint;
    private final String operator;

    /** Create a filter on a particular node pattern
     * 
     * @param node The node pattern to constrain
     * @param operator String representing some operator
     * @param constraint node pattern representing a constraint
     */
    public BasicNodeFilter(NodePattern<T> node, String operator, 
            NodePattern<T> constraint) {
        this.node = node;
        this.constraint = constraint;
        this.operator = operator;
    }
    
    public NodePattern<T> getNode() {
        return node;
    }
    
    public NodePattern<T> getConstraint() {
        return constraint;
    }
    
    public String getOperator() {
        return operator;
    }

}
