package org.nsdl.mptstore.query.component;

import java.util.ArrayList;
import java.util.List;


/** 
 * Representation of an RDF graph query.
 *
 * <p> A GraphQuery contains a sets of required and optional 
 * {@link QueryElement} components that form a single graph query.  Since a 
 * GraphQuery is also a QueryElement, graph queries can be arbitrarily 
 * nested.
 * </p>
 * 
 * @author birkland
 */
public class GraphQuery implements QueryElement {
    private ArrayList<QueryElement> required = new ArrayList<QueryElement>();
    private ArrayList<QueryElement> optional = new ArrayList<QueryElement>();

    /** {@inheritDoc} */
    public QueryElement.Type getType() {
        return QueryElement.Type.GraphQuery;
    }

    /**
     * Add a required query element.
     *
     * @param e the element to add.
     */
    public void addRequired(final QueryElement e) {
        required.add(e);
    }
    
    /**
     * Add an optional query element.
     *
     * @param e the element to add.
     */
    public void addOptional(final QueryElement e) {
        optional.add(e);
    }

    /**
     * Get the required query elements.
     *
     * @return the list of required elements.
     */
    public List<QueryElement> getRequired() {
        return new ArrayList<QueryElement>(required);
    }
    
    /**
     * Get the optional query elements.
     *
     * @return the list of optional elements.
     */
    public List<QueryElement> getOptional() {
        return new ArrayList<QueryElement>(optional);
    }
}
