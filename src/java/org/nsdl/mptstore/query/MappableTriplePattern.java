package org.nsdl.mptstore.query;

import java.util.HashSet;
import java.util.Set;

import org.nsdl.mptstore.rdf.Node;
import org.nsdl.mptstore.rdf.ObjectNode;
import org.nsdl.mptstore.rdf.PredicateNode;
import org.nsdl.mptstore.rdf.SubjectNode;

/** Represents a pattern that defines matching triples
 * <p>
 * A triple pattern contains three {@link org.nsdl.mptstore.query.MappableNodePattern} 
 * representing the subject, predicate, and object of a triple. 
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
class MappableTriplePattern implements TriplePattern {
	private final MappableNodePattern<SubjectNode> subject;
	private final MappableNodePattern<PredicateNode> predicate;
	private final MappableNodePattern<ObjectNode> object;
    
	public MappableTriplePattern(MappableNodePattern<SubjectNode> s, 
                                 MappableNodePattern<PredicateNode> p, 
                                 MappableNodePattern<ObjectNode> o) {
		
		this.subject = s;
		this.predicate = p;
		this.object = o;;
	}
	
    public MappableTriplePattern(TriplePattern pattern) {
        this.subject = new MappableNodePattern<SubjectNode>(pattern.getSubject());
        this.predicate = new MappableNodePattern<PredicateNode>(pattern.getPredicate());
        this.object = new MappableNodePattern<ObjectNode>(pattern.getObject());
    }
    
    public MappableNodePattern<SubjectNode> getSubject() {
        return this.subject;
    }
    
    public MappableNodePattern<PredicateNode> getPredicate() {
        return this.predicate;
    }
    
    public MappableNodePattern<ObjectNode> getObject() {
        return this.object;
    }
    
	public void bindTo(MPTable t) {
		subject.bindTo(t, MappableNodePattern.Types.subject);
		predicate.bindTo(t);
		object.bindTo(t, MappableNodePattern.Types.object);
	}

    public Set<MappableNodePattern<? extends Node>> getNodes() {
        Set<MappableNodePattern<? extends Node>> parts = new HashSet<MappableNodePattern<? extends Node>>();
        parts.add(subject);
        parts.add(object);
        return parts;
    }
	
	public String toString() {
		return subject + " " + predicate + " " + object;
	}
}
