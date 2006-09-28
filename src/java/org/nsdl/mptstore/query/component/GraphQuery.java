package org.nsdl.mptstore.query.component;

import java.util.ArrayList;
import java.util.List;

import org.nsdl.mptstore.query.component.QueryElement.Type;

/** Representation of an RDF graph query
 * <p> A GraphQuery contains a sets of required and optional {@link QueryElement} components that form a
 * single graph query.  Since a GraphQuery is also a QueryElement, graph queries can be arbitrarily 
 * nested 
 * </p>
 * 
 * @author birkland
 *
 */
public class GraphQuery implements QueryElement {
	private ArrayList<QueryElement> required = new ArrayList<QueryElement>();
	private ArrayList<QueryElement> optional = new ArrayList<QueryElement>();
	
	public QueryElement.Type getType() {
		return QueryElement.Type.GraphQuery;
	}
	
	public void addRequired(QueryElement e) {
		required.add(e);
	}
	
	public void addOptional(QueryElement e) {
		optional.add(e);
	}
	
	public List<QueryElement> getRequired() {
		return new ArrayList<QueryElement>(required);
	}
	
	public List<QueryElement> getOptional() {
		return new ArrayList<QueryElement>(optional);
	}
}
