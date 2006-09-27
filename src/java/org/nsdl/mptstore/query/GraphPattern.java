package org.nsdl.mptstore.query;

import java.util.ArrayList;
import java.util.List;

/** A set of concrete triple patterns and value constraints defining an 
 *  RDF subgraph
 * <p>
 * Triple patterns and filters ({@link NodeFilter} constraints on triple
 * value ranges) are as necessary to build a graph pattern
 * </p>
 * @author birkland
 *
 */
public class GraphPattern implements QueryElement {
	private ArrayList<TriplePattern> steps = new ArrayList<TriplePattern>();
	private ArrayList<NodeFilter> filters = new ArrayList<NodeFilter>();
	
	
	public QueryElement.Type getType() {
		return QueryElement.Type.GraphPattern;
	}
	
	
	public List<TriplePattern> getTriplePatterns() {
		return new ArrayList<TriplePattern>(steps);
	}
	
	
	public List<NodeFilter> getFilters() {
		return new ArrayList<NodeFilter>(filters);
	}
	
	
	public void addTriplePattern (TriplePattern s) {
		steps.add(s);
	}
	
	public void remove(TriplePattern s) {
		steps.remove(s);
	}
	
	public void addFilter(NodeFilter filter) {
		filters.add(filter);
	}
	
	public void removeFilter(String filter) {
		filters.remove(filter);
	}
	
 	public String toString() {
		StringBuilder outString = new StringBuilder();
		for (TriplePattern step : steps) {
			outString.append(step + " .\n");
		}
		
		return outString.toString();
	}
}
