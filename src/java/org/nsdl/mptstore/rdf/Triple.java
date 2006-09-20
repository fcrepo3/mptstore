package org.nsdl.mptstore.rdf;

public class Triple {

    private SubjectNode _subject;
    private PredicateNode _predicate;
    private ObjectNode _object;

    public Triple(SubjectNode subject,
                  PredicateNode predicate,
                  ObjectNode object) {
        _subject = subject;
        _predicate = predicate;
        _object = object;
    }

    public SubjectNode getSubject() {
        return _subject;
    }

    public PredicateNode getPredicate() {
        return _predicate;
    }

    public ObjectNode getObject() {
        return _object;
    }

    public String toString() {
        return _subject.toString() + " "
             + _predicate.toString() + " "
             + _object.toString() + " .";
    }

}
