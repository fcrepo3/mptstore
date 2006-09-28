package org.nsdl.mptstore.rdf;

/**
 * An RDF triple, also known as a statement.
 *
 * A triple consists of exactly one subject, predicate, and object.
 *
 * @author cwilper@cs.cornell.edu
 */
public class Triple {

    /**
     * The subject of this triple.
     */
    private SubjectNode _subject;

    /**
     * The predicate of this triple.
     */
    private PredicateNode _predicate;

    /**
     * The object of this triple.
     */
    private ObjectNode _object;

    /**
     * Construct a <code>Triple</code> with the given components.
     *
     * @param subject The subject.
     * @param predicate The predicate.
     * @param object The object.
     */
    public Triple(SubjectNode subject,
                  PredicateNode predicate,
                  ObjectNode object) {
        _subject = subject;
        _predicate = predicate;
        _object = object;
    }

    /**
     * Get the subject of this triple.
     *
     * @return the subject.
     */
    public SubjectNode getSubject() {
        return _subject;
    }

    /**
     * Get the predicate of this triple.
     *
     * @return the predicate.
     */
    public PredicateNode getPredicate() {
        return _predicate;
    }

    /**
     * Get the object of this triple.
     *
     * @return the object.
     */
    public ObjectNode getObject() {
        return _object;
    }

    /**
     * Get a string representation of this Triple, in N-Triples format.
     *
     * @return a space-delimited string consisting of the subject, predicate,
     *         and object strings, and ending with space dot.
     */
    public String toString() {
        return _subject.toString() + " "
             + _predicate.toString() + " "
             + _object.toString() + " .";
    }

}
