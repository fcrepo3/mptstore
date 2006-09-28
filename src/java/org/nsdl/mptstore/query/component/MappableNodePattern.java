package org.nsdl.mptstore.query.component;

import org.nsdl.mptstore.rdf.Node;
import org.nsdl.mptstore.rdf.ObjectNode;
import org.nsdl.mptstore.rdf.PredicateNode;
import org.nsdl.mptstore.rdf.SubjectNode;

/** Represents node pattern that can be mapped to a database object
 * <p>
 * Since a triple pattern may be mapped ap a specific table in the MPT model,
 * a mappable pattern node, then, can retain a mapping to a specific column
 * of a specific table.  Such a node has the following properties
 * <ul>
 *  <li> It may be a varible or a {@link org.nsdl.mptstore.rdf.Node}</li>
 *  <li> It may be mapped to a specific column of a specific table </li>
 *  <li> If it is a variable, there exists a mapping between the variable name 
 *   and its column name </li>
 * </ul>
 * </p>
 * @author birkland
 *
 * @param <T> The type of node that is described by the pattern.  Typically,
 * this is one of SubjectNode, PredicateNode, ObjectNode, or just Node if the
 * exact node type is unimportant or unknown.
 */
public class MappableNodePattern<T extends Node> implements NodePattern<T> {
    private final boolean isVariable;
    private final T nodeValue;
	private final String varName
    ;
	private MPTable boundTable;
	private String type;
	
    /** Create a node pattern that is a variable
     * 
     * @param variable any string representing the variable's name (e.g. "$value", "?person");
     * @param nodeClass the class of node this variable represents
     */
	public MappableNodePattern(String variable, String type) {
        
        this.varName = variable;
        this.isVariable = true;
        this.nodeValue = null;
        this.type = type;
	}
    
    /** Create a node pattern given a node value
     * 
     * @param node Node representing this pattern's value
     */
    public MappableNodePattern(T node) {
        if (node instanceof SubjectNode) {
            this.type = Types.subject;
        } else if (node instanceof ObjectNode) {
            this.type = Types.object;
        } else if (node instanceof PredicateNode) {
            this.type = Types.predicate;
        } else {
            throw new IllegalArgumentException("Given node type is not a " +
            "SubjectNode, PredicateNode, or ObjectNode" );
        }
        
        this.varName = null;
        this.isVariable = false;
        this.nodeValue = node;
    }
    
    public MappableNodePattern(NodePattern<? extends T> nodePattern) {
        
        this.varName = nodePattern.getVarName();
        this.isVariable = nodePattern.isVariable();
        this.nodeValue = nodePattern.getNode();
        
        if (!nodePattern.isVariable()) {
            T node = nodePattern.getNode();
            if (node instanceof SubjectNode) {
                this.type = Types.subject;
            } else if (node instanceof ObjectNode) {
                this.type = Types.object;
            } else if (node instanceof PredicateNode) {
                this.type = Types.predicate;
            } else {
                throw new IllegalArgumentException("Given node type is not a " +
                "SubjectNode, PredicateNode, or ObjectNode" );
            }
        } else {
            this.type = null;
        }

    }
    	
	public boolean isVariable() {
		return isVariable;
	}
	
    
	public void bindTo(MPTable t) {
        if (t == null) {
            throw new NullPointerException("Cannot bind to null table");
        }
		this.boundTable = t;
	}
	
    public void bindTo(MPTable t, String type) {
        if (t == null) {
            throw new NullPointerException("Cannot bind to null table");
        }
        
        if (this.type == null) {
            if (!type.matches("^" + Types.subject + "|" + Types.predicate + "|" + Types.object + "$")) {
                throw new IllegalArgumentException("Unknown type '" + type + ";");
            }
            this.type = type;
        }
        this.boundTable = t;
    }
    
	/**
	 * Return the table/alias and column identifier of the RDBMS location of this
	 * value or literal
	 */
	public String mappedName() {
		if (boundTable == null) {
            if (isVariable) {
                throw new RuntimeException("Variable " + varName + " has not been bound");
            } else {
                throw new RuntimeException("Node " + nodeValue + " has not been bound");
            }
		}
		
        if (this.type == null || this.type.equals(MappableNodePattern.Types.predicate)) {
            return boundTable.alias();
        } else { 
			return (boundTable.alias() + "." + type);
		} 
	}
	
    /** Returns the table to shich this pattern is bound
     * 
     */
	public MPTable boundTable() {
		return boundTable;
	}
	
	public T getNode() {
		return nodeValue;
	}
    
    public String getVarName() {
        return this.varName;
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
        if (!(p instanceof MappableNodePattern)) {return false;}
        MappableNodePattern comparison = (MappableNodePattern) p;
        
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
    
	public static class Types {
		public static final String subject = "s";
		public static final String predicate = "p";
		public static final String object = "o";
	}
}
