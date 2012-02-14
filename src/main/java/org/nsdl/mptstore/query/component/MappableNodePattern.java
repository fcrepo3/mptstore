package org.nsdl.mptstore.query.component;

import org.nsdl.mptstore.rdf.Node;
import org.nsdl.mptstore.rdf.ObjectNode;
import org.nsdl.mptstore.rdf.PredicateNode;
import org.nsdl.mptstore.rdf.SubjectNode;

/** Represents node pattern that can be mapped to a database object.
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
    private final String varName;
    private MPTable boundTable;
    private String theType;

    /** Create a node pattern that is a variable.
     *
     * @param variable any string representing the variable's name
     *        (e.g. "$value", "?person").
     * @param type the type of node this variable represents, which should be
     *        s, p, or o.
     */
    public MappableNodePattern(final String variable, final String type) {

        this.varName = variable;
        this.isVariable = true;
        this.nodeValue = null;
        theType = type;
    }

    /** Create a node pattern given a node value.
     *
     * @param node Node representing this pattern's value
     */
    public MappableNodePattern(final T node) {
        if (node instanceof SubjectNode) {
            theType = Types.SUBJECT;
        } else if (node instanceof ObjectNode) {
            theType = Types.OBJECT;
        } else if (node instanceof PredicateNode) {
            theType = Types.PREDICATE;
        } else {
            throw new IllegalArgumentException("Given node type is not a "
                    + "SubjectNode, PredicateNode, or ObjectNode");
        }

        this.varName = null;
        this.isVariable = false;
        this.nodeValue = node;
    }

    /**
     * Create a mappable node pattern from an existing node pattern.
     *
     * If the given pattern is variable, the type of this mappable node
     * pattern will be set to <code>null</code>.
     *
     * @param nodePattern the node pattern to base this one on.
     */
    public MappableNodePattern(final NodePattern<? extends T> nodePattern) {

        this.varName = nodePattern.getVarName();
        this.isVariable = nodePattern.isVariable();
        this.nodeValue = nodePattern.getNode();

        if (!nodePattern.isVariable()) {
            T node = nodePattern.getNode();
            if (node instanceof SubjectNode) {
                theType = Types.SUBJECT;
            } else if (node instanceof ObjectNode) {
                theType = Types.OBJECT;
            } else if (node instanceof PredicateNode) {
                theType = Types.PREDICATE;
            } else {
                throw new IllegalArgumentException("Given node type is not a "
                        + "SubjectNode, PredicateNode, or ObjectNode");
            }
        } else {
            theType = null;
        }

    }
    /**
     * Create a mappable node pattern from an existing node pattern.
     *
     * If the given pattern is variable, the type of this mappable node
     * pattern will be set to <code>null</code>.
     *
     * @param nodePattern the node pattern to create the mappable pattern from.
     * @param patternType constraint on the type of node allowed by this
     *        pattern.
     */
    public MappableNodePattern(final NodePattern<? extends T> nodePattern,
                               final Class<T> patternType) {

        this.varName = nodePattern.getVarName();
        this.isVariable = nodePattern.isVariable();
        this.nodeValue = nodePattern.getNode();


        if (SubjectNode.class.isAssignableFrom(patternType)) {
            theType = Types.SUBJECT;
        } else if (ObjectNode.class.isAssignableFrom(patternType)) {
            theType = Types.OBJECT;
        } else if (PredicateNode.class.isAssignableFrom(patternType)) {
            theType = Types.PREDICATE;
        } else {
            throw new IllegalArgumentException("Given node type is not a "
                    + "SubjectNode, PredicateNode, or ObjectNode");
        }
    }

    /**
     * Tell whether this node pattern is variable.
     *
     * @return true if it is variable, false otherwise.
     */
    public boolean isVariable() {
        return isVariable;
    }

    /**
     * Bind this node pattern to the given table.
     *
     * @param t the table.
     */
    public void bindTo(final MPTable t) {
        if (t == null) {
            throw new NullPointerException("Cannot bind to null table");
        }
        this.boundTable = t;
    }

    /**
     * Bind this node pattern to the given table and optionally set the type.
     *
     * @param t the table.
     * @param type the new type for this node pattern.  This should be s, p,
     *        o, or <code>null</code> if the type shouldn't be set.
     */
    public void bindTo(final MPTable t, final String type) {
        if (t == null) {
            throw new NullPointerException("Cannot bind to null table");
        }

        if (theType == null) {
            if (!type.matches("^" + Types.SUBJECT + "|" + Types.PREDICATE
                    + "|" + Types.OBJECT + "$")) {
                throw new IllegalArgumentException("Unknown type '"
                        + type + "'");
            }
            theType = type;
        }
        this.boundTable = t;
    }

    /**
     * Return the table/alias and column identifier of the RDBMS location of
     * this value or literal.
     *
     * @return the mapped name.
     */
    public String mappedName() {
        if (boundTable == null) {
            if (isVariable) {
                throw new RuntimeException("Variable " + varName
                        + " has not been bound");
            } else {
                throw new RuntimeException("Node " + nodeValue
                        + " has not been bound");
            }
        }

        if (theType == null || theType.equals(
                MappableNodePattern.Types.PREDICATE)) {
            return boundTable.alias();
        } else {
            return (boundTable.alias() + "." + theType);
        }
    }

    /** Return the table to which this pattern is bound.
     *
     * @return the table this is bound to.
     */
    public MPTable boundTable() {
        return boundTable;
    }

    /** {@inheritDoc} */
    public T getNode() {
        return nodeValue;
    }

    /** {@inheritDoc} */
    public String getVarName() {
        return this.varName;
    }

    /** Equality of Triple Pattern.
     *  <p>
     *  Equality of triple patterns follow the following rules:
     *  <ul>
     *   <li> They need to be the same type (e.g. variables or nodes).</li>
     *   <li> Nulls are not equal.</li>
     *   <li> If both patterns are Nodes, equality is determined by comparing
     *   getValue() for each node.</li>
     *  </ul>
     *  </p>
     * @param p the object to compare this one to.
     * @return whether the objects are equal according to the above rules.
     */
    public boolean equals(final Object p) {
        if (!(p instanceof MappableNodePattern)) {
            return false;
        }
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
                return this.getNode().getValue().equals(
                        comparison.getNode().getValue());
            } catch (NullPointerException e) {
                return false;
            }
        }
        return false;
    }

    /**
     * Get a string representation of this mappable node pattern.
     *
     * If variable, this is the variable.
     * Otherwise, it's the node's string representation.
     *
     * @return the string representation.
     */
    public String toString() {
        if (isVariable) {
            return varName;
        } else {
            return nodeValue.toString();
        }
    }

    /**
     * Get a hash code for this mappable node pattern.
     *
     * If variable, the hash code is that of the variable string.
     * Otherwise, it's the hash code of the node.
     *
     * @return the hash code.
     */
    public int hashCode() {
        if (isVariable) {
            return varName.hashCode();
        } else {
            return nodeValue.hashCode();
        }
    }

    /**
     * Types of <code>MappableNodePattern</code>.
     */
    public static class Types {

        /** Indicates a subject. */
        public static final String SUBJECT = "s";

        /** Indicates a predicate. */
        public static final String PREDICATE = "p";

        /** Indicates an object. */
        public static final String OBJECT = "o";
    }
}
