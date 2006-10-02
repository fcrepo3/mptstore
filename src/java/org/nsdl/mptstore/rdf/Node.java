package org.nsdl.mptstore.rdf;

/**
 * Common interface for all RDF subjects, predicates, and objects.
 *
 * @author cwilper@cs.cornell.edu
 */
public interface Node {

    /**
     * Get the lexical value of the node.
     * <p>
     *   The lexical value of a <code>URIReference</code> node is the URI 
     *   as a string.
     * </p>
     * <p>
     *   The lexical value of a <code>Literal</code> node is a string 
     *   representation of the value, and does not include the language or 
     *   data type, if any.
     * </p>
     *
     * @return the lexical value, never <code>null</code>.
     */
    public String getValue();

    /**
     * Get an N-Triples string representing this node.
     * <p>
     *   For <code>URIReference</code> nodes, this is &lt;$uri&gt;.
     * </p>
     * <p>
     *   For Literal nodes, this is one of the following:
     *   <ul>
     *     <li> "$value"</li>
     *     <li> "$value"@$lang</li>
     *     <li> "$value"^^&lt;$datatypeURI&gt;</li>
     *   </ul>
     * </p>
     * <p>
     *   Note:
     *   <ul> 
     *     <li> The returned string will consist of only 7-bit ASCII
     *          characters.</li>
     *     <li> The $value part will be escaped according to the rules 
     *          set out by the N-Triples format.</li>
     *   </ul>
     * </p>
     *
     * @return the N-Triples representation of the node.
     * @see <a href="http://www.w3.org/TR/rdf-testcases/#ntriples">
     *        <em>RDF Test Cases, Section 3: N-Triples</em></a>
     */
    public String toString();

    /**
     * Tell whether this <code>Node</code> should be considered equivalent
     * to the given object.
     * <p>
     *   A <code>URIReference</code> node is equal to the given 
     *   object if all of the following conditions are met:
     *   <ul>
     *     <li> <code>obj != null</code></li>
     *     <li> <code>obj instanceof URIReference</code></li>
     *     <li> <code>obj.getURI().equals(this.getURI())</code></li>
     *   </ul>
     * </p>
     * <p>
     *   A <code>Literal</code> node is equal to the given 
     *   object if all of the following conditions are met:
     *   <ul>
     *     <li> <code>obj != null</code></li>
     *     <li> <code>obj instanceof Literal</code></li>
     *     <li> <code>obj.getValue().equals(this.getValue())</code></li>
     *     <li> The given literal's language (or null) is equal to this
     *          literal's language (or null).</li>
     *     <li> The given literal's datatype (or null) is equal to this
     *          literal's datatype (or null).</li>
     *   </ul>
     * </p>
     *
     * @param obj the object to compare to this one.
     * @return true if the objects are equal according to the rules above.
     */
    public boolean equals(Object obj);

    /**
     * Return a hash code for this node.
     * <p>
     *   The hash code of a URIReference is the hash code of getURI().
     *   The hash code of a Literal is the hash code of getValue().
     * </p>
     *
     * @return the hash code.
     */
    public int hashCode();

}
