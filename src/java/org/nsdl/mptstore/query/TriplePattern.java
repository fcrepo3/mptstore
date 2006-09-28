package org.nsdl.mptstore.query;


import org.nsdl.mptstore.rdf.ObjectNode;
import org.nsdl.mptstore.rdf.PredicateNode;
import org.nsdl.mptstore.rdf.SubjectNode;

/** Represents a pattern that defines matching triples
 * <p>
 * A triple pattern contains three {@link org.nsdl.mptstore.query.NodePattern}s 
 * representing the subject, predicate, and object of a triple.  
 * </p>
 * @author birkland
 *
 */
public interface TriplePattern {
    
    /** Get the subject pattern of a triple */
    public NodePattern<SubjectNode> getSubject();
    
    /** Get the predicate pattern of a triple */
    public NodePattern<PredicateNode> getPredicate();
    
    /** Get the Object pattern of a triple */
    public NodePattern<ObjectNode> getObject();
    
}
