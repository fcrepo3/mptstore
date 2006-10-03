package org.nsdl.mptstore.query.component;


import org.nsdl.mptstore.rdf.ObjectNode;
import org.nsdl.mptstore.rdf.PredicateNode;
import org.nsdl.mptstore.rdf.SubjectNode;

/** Generic implementation of {@link TriplePattern}.
 *
 * @author birkland
 *
 */
public class BasicTriplePattern implements TriplePattern {
    private final NodePattern<SubjectNode> subject;
    private final NodePattern<PredicateNode> predicate;
    private final NodePattern<ObjectNode> object;

    /**
     * Construct a BasicTriplePattern.
     *
     * @param s the subject pattern.
     * @param p the predicate pattern.
     * @param o the object pattern.
     */
    public BasicTriplePattern(final NodePattern<SubjectNode> s,
                              final NodePattern<PredicateNode> p,
                              final NodePattern<ObjectNode> o) {
        this.subject = s;
        this.predicate = p;
        this.object = o;
    }

    /** {@inheritDoc} */
    public NodePattern<SubjectNode> getSubject() {
        return this.subject;
    }

    /** {@inheritDoc} */
    public NodePattern<ObjectNode> getObject() {
        return this.object;
    }

    /** {@inheritDoc} */
    public NodePattern<PredicateNode> getPredicate() {
        return this.predicate;
    }

    /** {@inheritDoc} */
    public String toString() {
        return subject + " " + predicate + " " + object;
    }
}
