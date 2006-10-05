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
    public Triple(final SubjectNode subject,
                  final PredicateNode predicate,
                  final ObjectNode object) {
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

    /**
     * Tell whether the given object is equal to this triple.
     *
     * @param obj the object to compare with this one.
     * @return true if it's a Triple and the subject, predicate, and
     *         object all compare the same.
     */
    public boolean equals(final Object obj) {
        if (obj instanceof Triple) {
            Triple triple = (Triple) obj;
            return (triple.getSubject().equals(_subject)
                    && triple.getPredicate().equals(_predicate)
                    && triple.getObject().equals(_object));
        } else {
            return false;
        }
    }

    /**
     * Return a hash code for this triple.
     *
     * The hash code of a <code>Triple</code> is the sum of the subject,
     * predicate, and object components.
     *
     * @return the hashcode.
     */
    public int hashCode() {
        return _subject.hashCode() + _predicate.hashCode() + _object.hashCode();
    }

}
