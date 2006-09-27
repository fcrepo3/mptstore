package org.nsdl.mptstore.query;

import org.nsdl.mptstore.rdf.Node;

/** Generic  implementation of a {@link org.nsdl.mptstore.query.NodeFilter} 
 * 
 * 
 * @author birkland
 */
public class BasicNodeFilter implements NodeFilter {
    private final NodePattern<? extends Node> node;
    private final NodePattern<? extends Node> constraint;
    private final String operator;

    /** Create a filter on a particular node pattern
     * 
     * @param node The node pattern to constrain
     * @param operator String representing some operator
     * @param constraint node pattern representing a constraint
     */
    public BasicNodeFilter(NodePattern<? extends Node> node, String operator, 
            NodePattern<? extends Node> constraint) {
        this.node = node;
        this.constraint = constraint;
        this.operator = operator;
    }
    
    public NodePattern<? extends Node> getNode() {
        return node;
    }
    
    public NodePattern<? extends Node> getConstraint() {
        return constraint;
    }
    
    public String getOperator() {
        return operator;
    }

}
