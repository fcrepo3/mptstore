package org.nsdl.mptstore.query;

import java.util.HashSet;
import java.util.Set;

/** Represents a pattern that defines matching triples
 * <p>
 * A triple pattern contains three TriplePatternNodes representing
 * the subject, predicate, and object of a triple.  Each node may
 * be a literal, URI, or variable ({@link org.nsdl.mptstore.query.TriplePatternNode})
 * </p>
 * <p>
 * In addition to representing a particular triple pattern, a TriplePattern may
 * be bound to a apecific table (logically, the one that maps to its predicate). 
 * When bound to a table, the TriplePattern may be used for selecting triples from its 
 * table or for joining triple selection results with other TriplePatterns.
 * 
 * </p>
 * @author birkland
 *
 */
public class TriplePattern {
	public final TriplePatternNode subject;
	public final TriplePatternNode predicate;
	public final TriplePatternNode object;
    
	public TriplePattern(String s, String p, String o) {
		TriplePatternNode pNode = 
            new TriplePatternNode(p, TriplePatternNode.Types.predicate);
		if (!pNode.isURI()) {
			throw new IllegalArgumentException("Predicate must be a URI");
		}
		this.subject = new TriplePatternNode(s, TriplePatternNode.Types.subject);
		this.predicate = pNode;
		this.object = new TriplePatternNode(o, TriplePatternNode.Types.object);
	}
	
	public void bindTo(MPTable t) {
		subject.bindTo(t);
		predicate.bindTo(t);
		object.bindTo(t);
	}
    
    public Set<TriplePatternNode> getNodes() {
        Set<TriplePatternNode> parts = new HashSet<TriplePatternNode>();
        parts.add(subject);
        parts.add(object);
        return parts;
    }
	
	public String toString() {
		return subject.value() + " " + predicate.value() + " " + object.value();
	}
}
