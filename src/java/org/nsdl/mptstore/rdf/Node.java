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

}
