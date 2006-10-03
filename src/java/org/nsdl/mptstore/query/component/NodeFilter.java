package org.nsdl.mptstore.query.component;

import org.nsdl.mptstore.rdf.Node;

/** 
 * Represents a constraint on the value of a particular node pattern.
 * <p>
 * Relates a node pattern to a particular constraint value via 
 * some operator.  The actual meaning of the constraint is 
 * determined by whatever query engine/language is using 
 * the node filter.  This class merely represents a generic container
 * used for constraining node values in queries.
 * </p>
 *
 * @param <T> The type of node that is being constrained by this filter.
 *            Typically, this is one of SubjectNode, PredicateNode, 
 *            ObjectNode, or just Node if the exact node type is unimportant 
 *            or unknown.
 * @author birkland
 */
public interface NodeFilter<T extends Node> {

    /** 
     * Get the node pattern whose value this NodeFilter is constraining.
     *
     * @return the pattern.
     */
    NodePattern<T> getNode();
    
    /** 
     * Get the operator that specifies the relationship between the NodePattern 
     * and a value constraint (e.g. '=', '<', '>', etc).  Its semantics exact 
     * are undefined at this level.
     *
     * @return the operator.
     */
    String getOperator();
    
    /** 
     * Get the constraint on this filter's NodePattern's value.
     *
     * @return the constraint.
     */
    NodePattern<T> getConstraint();
}
