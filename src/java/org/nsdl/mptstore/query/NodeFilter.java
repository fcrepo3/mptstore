package org.nsdl.mptstore.query;

import org.nsdl.mptstore.rdf.Node;

/** Represents a constraint on the value of a particular node pattern
* <p>
* Relates a node pattern to a particular constraint value via 
* some operator.  The actual meaning of the constraint is 
* determined by whatever query engine/language is using 
* the node filter.  This class merely represents a generic container
* used for constraining node values in queries.
*</p>
*
*@author birkland
*/
public interface NodeFilter
{
    /** The node pattern whose value this NodeFilter is constraining */
    public NodePattern<? extends Node> getNode();
    
    /** Operator that specifies the relationship between the NodePattern and
     * a value constraint (e.g. '=', '<', '>', etc).  Its semantics exact are
     * undefined at this level.
     */
    public String getOperator();
    
    /** Represents some constraint on this filter's NodePattern's value */
    public NodePattern<? extends Node> getConstraint();
}
