package org.nsdl.mptstore.query;

import java.util.ArrayList;
import java.util.List;

/** A set of concrete triple patterns and value constraints defining an 
 *  RDF subgraph
 * <p>
 * Triple patterns and filters ({@link TripleFilter} coonstraints on triple
 * value ranges) are added one by one to create a graph pattern 
 * </p>
 * @author birkland
 *
 */
public class GraphPattern implements QueryElement {
	private ArrayList<TriplePattern> steps = new ArrayList<TriplePattern>();
	private ArrayList<TripleFilter> filters = new ArrayList<TripleFilter>();
	
	
	public QueryElement.Type getType() {
		return QueryElement.Type.GraphPattern;
	}
	
	
	public List<TriplePattern> getTriplePatterns() {
		return new ArrayList<TriplePattern>(steps);
	}
	
	
	public List<TripleFilter> getFilters() {
		return new ArrayList<TripleFilter>(filters);
	}
	
	
	public void addTriplePattern (TriplePattern s) {
		steps.add(s);
	}
	
	public void remove(TriplePattern s) {
		steps.remove(s);
	}
	
	public void addFilter(TripleFilter filter) {
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
