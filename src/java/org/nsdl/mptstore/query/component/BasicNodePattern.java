package org.nsdl.mptstore.query.component;

import org.nsdl.mptstore.rdf.Node;

/** Generic implementation of {@link org.nsdl.mptstore.query.component.NodePattern}
 * 
 * @author birkland
 *
 * @param <T> The type of node that is described by the pattern.  Typically,
 * this is one of SubjectNode, PredicateNode, ObjectNode, or just Node if the
 * exact node type is unimportant or unknown.
 */
public class BasicNodePattern<T extends Node> implements NodePattern<T> {
    
    private final T nodeValue;
    private final boolean isVariable;
    private final String varName;
    
    
    public BasicNodePattern(T node) {
        this.nodeValue = node;
        this.isVariable = false;
        this.varName = null;
    }
    
    public BasicNodePattern(String variable) {
        this.nodeValue = null;
        this.isVariable = true;
        this.varName = variable;
    }
    
    public T getNode() {
        return this.nodeValue;
    }
    
    public String getVarName() {
        return this.varName;
    }

    public boolean isVariable() {
        return this.isVariable;
    }
    
    /** Equality of Triple Pattern
     *  <p>
     *  Equality of triple patterns follow the following rules:
     *  <ul>
     *   <li> They both need to be the same type (e.g. variables or nodes) </li>
     *   <li> Nulls are not equal </li>
     *   <li> If both patterns are Nodes, equality is determined by comparing
     *   getValue() for each node.  
     *  </ul>
     *  </p>
     */
    public boolean equals(Object p) {
        if (!(p instanceof NodePattern)) {return false;}
        NodePattern comparison = (NodePattern) p;
        
        if (this.isVariable() && comparison.isVariable()) {
            if (this.getVarName() == null || comparison.getVarName() == null) {
                return false;
            }
            return this.getVarName().equals(comparison.getVarName());
        } else if ((!this.isVariable()) && (!comparison.isVariable())) {
            if (this.getNode() == null || comparison.getNode() == null) {
                return false;
            }
            try {
                return this.getNode().getValue().equals(comparison.getNode().getValue());
            } catch (NullPointerException e) {
                return false;
            }
        } 
        return false;
    }
    
    public String toString() {
        if (isVariable) {
            return varName;
        } else {
            return nodeValue.toString();
        }
    }
}
