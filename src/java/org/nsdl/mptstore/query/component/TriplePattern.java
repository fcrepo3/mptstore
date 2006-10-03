package org.nsdl.mptstore.query.component;

import org.nsdl.mptstore.rdf.ObjectNode;
import org.nsdl.mptstore.rdf.PredicateNode;
import org.nsdl.mptstore.rdf.SubjectNode;

/** 
 * Represents a pattern that defines matching triples.
 * <p>
 * A triple pattern contains three {@link NodePattern}s representing the 
 * subject, predicate, and object of a triple.  
 * </p>
 *
 * @author birkland
 */
public interface TriplePattern {
    
    /** 
     * Get the subject pattern of this triple pattern.
     *
     * @return the subject pattern.
     */
    NodePattern<SubjectNode> getSubject();
    
    /** 
     * Get the predicate pattern of this triple pattern.
     *
     * @return the predicate pattern.
     */
    NodePattern<PredicateNode> getPredicate();
    
    /** 
     * Get the object pattern of this triple pattern.
     *
     * @return the object pattern.
     */
    NodePattern<ObjectNode> getObject();
    
}
