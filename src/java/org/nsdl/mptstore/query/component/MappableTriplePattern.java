package org.nsdl.mptstore.query.component;

import java.util.HashSet;
import java.util.Set;

import org.nsdl.mptstore.rdf.Node;
import org.nsdl.mptstore.rdf.ObjectNode;
import org.nsdl.mptstore.rdf.PredicateNode;
import org.nsdl.mptstore.rdf.SubjectNode;

/**
 * Represents a pattern that defines matching triples.
 * <p>
 * A triple pattern contains three {@link MappableNodePattern}s
 * representing the subject, predicate, and object of a triple.
 * </p>
 * <p>
 * In addition to representing a particular triple pattern, a TriplePattern
 * may be bound to a apecific table (logically, the one that maps to its
 * predicate).  When bound to a table, the TriplePattern may be used for
 * selecting triples from its table or for joining triple selection results
 * with other TriplePatterns.
 * </p>
 *
 * @author birkland
 */
public class MappableTriplePattern implements TriplePattern {
    private final MappableNodePattern<SubjectNode> subject;
    private final MappableNodePattern<PredicateNode> predicate;
    private final MappableNodePattern<ObjectNode> object;

    /**
     * Construct a mappable triple pattern with the given component
     * patterns.
     *
     * @param s the subject pattern.
     * @param p the predicate pattern.
     * @param o the object pattern.
     */
    public MappableTriplePattern(final MappableNodePattern<SubjectNode> s,
                                 final MappableNodePattern<PredicateNode> p,
                                 final MappableNodePattern<ObjectNode> o) {

        this.subject = s;
        this.predicate = p;
        this.object = o;
    }

    /**
     * Construct a mappable triple pattern using the existing triple pattern.
     *
     * @param pattern the existing triple pattern.
     */
    public MappableTriplePattern(final TriplePattern pattern) {
        this.subject = new MappableNodePattern<SubjectNode>(
                pattern.getSubject(), SubjectNode.class);
        this.predicate = new MappableNodePattern<PredicateNode>(
                pattern.getPredicate(), PredicateNode.class);
        this.object = new MappableNodePattern<ObjectNode>(
                pattern.getObject(), ObjectNode.class);
    }

    /**
     * Get the subject pattern.
     *
     * @return the subject pattern.
     */
    public MappableNodePattern<SubjectNode> getSubject() {
        return this.subject;
    }

    /**
     * Get the predicate pattern.
     *
     * @return the predicate pattern.
     */
    public MappableNodePattern<PredicateNode> getPredicate() {
        return this.predicate;
    }

    /**
     * Get the object pattern.
     *
     * @return the object pattern.
     */
    public MappableNodePattern<ObjectNode> getObject() {
        return this.object;
    }

    /**
     * Bind this triple pattern to the given table.
     *
     * @param t the table to bind to.
     */
    public void bindTo(final MPTable t) {
        subject.bindTo(t, MappableNodePattern.Types.SUBJECT);
        predicate.bindTo(t);
        object.bindTo(t, MappableNodePattern.Types.OBJECT);
    }

    /**
     * Get the subject and object node patterns in a set.
     *
     * @return a set containing the subject and object node patterns.
     */
    public Set<MappableNodePattern<? extends Node>> getNodes() {
        Set<MappableNodePattern<? extends Node>> parts =
                new HashSet<MappableNodePattern<? extends Node>>();
        parts.add(subject);
        parts.add(object);
        return parts;
    }

    /** {@inheritDoc} */
    public String toString() {
        return subject + " " + predicate + " " + object;
    }
}
