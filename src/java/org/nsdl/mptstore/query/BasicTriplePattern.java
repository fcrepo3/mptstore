package org.nsdl.mptstore.query;


import org.nsdl.mptstore.rdf.ObjectNode;
import org.nsdl.mptstore.rdf.PredicateNode;
import org.nsdl.mptstore.rdf.SubjectNode;

/** Generic implementation of {@link org.nsdl.mptstore.query.TriplePattern}
 * 
 * @author birkland
 *
 */
public class BasicTriplePattern implements TriplePattern {
    private final NodePattern<SubjectNode> subject;
    private final NodePattern<PredicateNode> predicate;
    private final NodePattern<ObjectNode> object;
    
    public BasicTriplePattern(NodePattern<SubjectNode> s, 
                         NodePattern<PredicateNode> p, 
                         NodePattern<ObjectNode> o) {
        this.subject = s;
        this.predicate = p;
        this.object = o;
    }
    
    public NodePattern<SubjectNode> getSubject() {
        return this.subject;
    }
    
    public NodePattern<ObjectNode> getObject() {
        return this.object;
    }
    
    public NodePattern<PredicateNode> getPredicate() {
        return this.predicate;
    }
    
    public String toString() {
        return subject + " " + predicate + " " + object;
    }
}
