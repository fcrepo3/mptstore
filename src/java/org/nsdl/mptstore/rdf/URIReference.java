package org.nsdl.mptstore.rdf;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * An RDF URI reference.
 *
 * This is an absolute URI with an optional fragment identifier.
 * A <code>URIReference</code> can play the part of a subject, predicate,
 * or object in an RDF triple.
 *
 * @author cwilper@cs.cornell.edu
 */
public class URIReference
        implements SubjectNode, PredicateNode, ObjectNode {

    /**
     * The absolute URI.
     */
    private URI _uri;

    /**
     * Construct a <code>URIReference</code> given an existing URI.
     *
     * @param uri The existing URI.
     * @throws URISyntaxException if the URI is not absolute.
     */
    public URIReference(URI uri) throws URISyntaxException {
        if (uri.isAbsolute()) {
            _uri = uri;
        } else {
            throw new URISyntaxException(uri.toString(), "not absolute");
        }
    }

    /**
     * Construct a <code>URIReference</code> given a URI string.
     *
     * @param uri The URI string.
     * @throws URISyntaxException if the given string is not a valid URI
     *         or is not absolute.
     */
    public URIReference(String uri) throws URISyntaxException {
        this(new URI(uri));
    }

    /**
     * Get the URI.
     *
     * @return the URI.
     */
    public URI getURI() {
        return _uri;
    }

    // Implements Node.getValue()
    public String getValue() {
        return _uri.toString();
    }

    // Implements Node.toString()
    public String toString() {
        return "<" + _uri.toString() + ">";
    }

    // Implements Node.equals(Object)
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof URIReference) {
            return _uri.equals(((URIReference) obj).getURI());
        } else {
            return false;
        }
    }

    // Implements Node.hashCode()
    public int hashCode() {
        return _uri.hashCode();
    }

}
