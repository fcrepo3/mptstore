package org.nsdl.mptstore.query;

import java.util.HashSet;
import java.util.Set;

import org.nsdl.mptstore.rdf.ObjectNode;
import org.nsdl.mptstore.rdf.PredicateNode;
import org.nsdl.mptstore.rdf.SubjectNode;

/** Represents a pattern that defines matching triples
 * <p>
 * A triple pattern contains three @link{org.nsdl.mptstore.query.NodePattern}s 
 * representing the subject, predicate, and object of a triple.  
 * </p>
 * @author birkland
 *
 */
public class TriplePattern {
	public final NodePattern<SubjectNode> subject;
	public final NodePattern<PredicateNode> predicate;
	public final NodePattern<ObjectNode> object;
    
	public TriplePattern(NodePattern<SubjectNode> s, 
                         NodePattern<PredicateNode> p, 
                         NodePattern<ObjectNode> o) {
		this.subject = s;
        this.predicate = p;
        this.object = o;
	}
	
	
    public Set<NodePattern> getNodes() {
        Set<NodePattern> parts = new HashSet<NodePattern>();
        parts.add(subject);
        parts.add(object);
        return parts;
    }
	
	public String toString() {
		return subject + " " + predicate + " " + object;
	}
}
