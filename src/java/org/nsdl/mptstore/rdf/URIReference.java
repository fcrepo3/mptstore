package org.nsdl.mptstore.rdf;

import java.net.URI;
import java.net.URISyntaxException;

public class URIReference
        implements SubjectNode, PredicateNode, ObjectNode {

    private URI _uri;

    public URIReference(URI uri) throws URISyntaxException {
        if (uri.isAbsolute()) {
            _uri = uri;
        } else {
            throw new URISyntaxException(uri.toString(), "not absolute");
        }
    }

    public URIReference(String uri) throws URISyntaxException {
        this(new URI(uri));
    }

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

}
