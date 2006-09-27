package org.nsdl.mptstore.query;

import org.nsdl.mptstore.rdf.Node;

/** Represents a specified or unspecified (variable) in a query or pattern
 * 
 * <p>
 * A NodePattern is a representation of a {@link org.nsdl.mptstore.rdf.Node} 
 * in a query, which may have given value.  If a NodePattern does not have
 * an explicit Node value, then it represents variable.  Variables may have
 * names associated with them that may have a meaning 
 * the context of a particular query language.
 * </p>
 * 
 * @author birkland
 *
 * @param <T>
 */
public interface NodePattern<T extends Node> {
    
    /** Get the node value ofthis pattern, or null if this pattern
     * does not represent a variable
     * 
     * @return the node value
     */
    public T getNode();
    
    /** Get the variable name of this pattern, or null if this pattern
     * does not represent a variable 
     * @return the variable name
     */
    public String getVarName();
    
    /** Determind if this node pattern represents a variable */
    public boolean isVariable();
}
