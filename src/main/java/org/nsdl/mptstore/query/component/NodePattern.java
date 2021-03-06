package org.nsdl.mptstore.query.component;

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
 * @param <T> The type of node that is described by the pattern.  Typically,
 * this is one of SubjectNode, PredicateNode, ObjectNode, or just Node if the
 * exact node type is unimportant or unknown.
 */
public interface NodePattern<T extends Node> {

    /** Get the node value of this pattern, or null if this pattern
     * does not represent a variable.
     *
     * @return the node value
     */
    T getNode();

    /** Get the variable name of this pattern, or null if this pattern
     * does not represent a variable.
     *
     * @return the variable name
     */
    String getVarName();

    /**
     * Tell whether this node pattern represents a variable.
     *
     * @return whether this pattern represents a variable.
     */
    boolean isVariable();
}
