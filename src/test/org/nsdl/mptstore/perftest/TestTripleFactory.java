package org.nsdl.mptstore.perftest;

import java.net.URISyntaxException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.nsdl.mptstore.rdf.Literal;
import org.nsdl.mptstore.rdf.SubjectNode;
import org.nsdl.mptstore.rdf.Triple;
import org.nsdl.mptstore.rdf.URIReference;

public class TestTripleFactory {

    private final static String XSD_NS= "http://www.w3.org/2001/XMLSchema#";

    private final static URIReference XSD_LONG = 
            getURIReference(XSD_NS+ "long");

    private final static URIReference XSD_DOUBLE = 
            getURIReference(XSD_NS+ "double");

    private final static URIReference XSD_DATETIME =
            getURIReference(XSD_NS+ "dateTime");

    private final static DateFormat FORMATTER = 
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    private final int _relsPerSubject;
    private final int _plainsPerSubject;
    private final int _localsPerSubject;
    private final int _longsPerSubject;
    private final int _doublesPerSubject;
    private final int _dateTimesPerSubject;

    private int _currentResource;
    private long _currentLong;
    private double _currentDouble;
    private long _currentMillis;

    public TestTripleFactory(final int relsPerSubject, 
            final int plainsPerSubject, final int localsPerSubject,
            final int longsPerSubject, final int doublesPerSubject,
            final int dateTimesPerSubject) {
        _relsPerSubject = relsPerSubject;
        _plainsPerSubject = plainsPerSubject;
        _localsPerSubject = localsPerSubject;
        _longsPerSubject = longsPerSubject;
        _doublesPerSubject = doublesPerSubject;
        _dateTimesPerSubject = dateTimesPerSubject;
    }

    public int getRelsPerSubject()      { return _relsPerSubject; }
    public int getPlainsPerSubject()    { return _plainsPerSubject; }
    public int getLocalsPerSubject()    { return _localsPerSubject; }
    public int getLongsPerSubject()     { return _longsPerSubject; }
    public int getDoublesPerSubject()   { return _doublesPerSubject; }
    public int getDateTimesPerSubject() { return _dateTimesPerSubject; }

    public synchronized Set<Triple> getNextSet() {
        _currentResource++;
        Set<Triple> set = new HashSet<Triple>();
        URIReference subject = getResource(_currentResource);
        addRels(subject, set);
        addPlains(subject, set);
        addLocals(subject, set);
        addLongs(subject, set);
        addDoubles(subject, set);
        addDateTimes(subject, set);
        return set;
    }

    private void addRels(final SubjectNode subject, final Set<Triple> set) {
        for (int i = 1; i <= _relsPerSubject; i++) {
            set.add(new Triple(subject, getURIReference("urn:rel:" + i),
                    getResource(_currentResource + i)));
        }
    }

    private void addPlains(final SubjectNode subject, final Set<Triple> set) {
        for (int i = 1; i <= _plainsPerSubject; i++) {
            set.add(new Triple(subject, getURIReference("urn:plain:" + i),
                    new Literal("plain " + i + " for resource " 
                    + _currentResource)));
        }
    }

    private void addLocals(final SubjectNode subject, final Set<Triple> set) {
        for (int i = 1; i <= _localsPerSubject; i++) {
            set.add(new Triple(subject, getURIReference("urn:local:" + i),
                    getLocalLiteral("local for resource "
                    + _currentResource, i)));
        }
    }

    private void addLongs(final SubjectNode subject, final Set<Triple> set) {
        for (int i = 1; i <= _longsPerSubject; i++) {
            _currentLong++;
            set.add(new Triple(subject, getURIReference("urn:long:" + i),
                    new Literal("" + _currentLong, XSD_LONG)));
        }
    }

    private void addDoubles(final SubjectNode subject, final Set<Triple> set) {
        for (int i = 1; i <= _doublesPerSubject; i++) {
            _currentDouble += 0.01;
            set.add(new Triple(subject, getURIReference("urn:double:" + i),
                    new Literal("" + _currentDouble, XSD_DOUBLE)));
        }
    }

    private void addDateTimes(final SubjectNode subject, 
            final Set<Triple> set) {
        for (int i = 1; i <= _dateTimesPerSubject; i++) {
            _currentMillis++;
            set.add(new Triple(subject, getURIReference("urn:dateTime:" + i),
                    getDateTimeLiteral(_currentMillis)));
        }
    }

    private static final URIReference getResource(final int id) {
        return getURIReference("urn:resource:" + id);
    }

    private static final URIReference getURIReference(final String uri) {
        try {
            return new URIReference(uri);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Error: Bad URI: " + uri);
        }
    }

    private static final Literal getLocalLiteral(final String lex, 
            final int langNum) {
        try {
            return new Literal(lex, "lang-" + langNum);
        } catch (ParseException e) {
            throw new RuntimeException("Error: Bad Lang: " + langNum);
        }
    }

    private static final Literal getDateTimeLiteral(final long millis) {
        String lex = FORMATTER.format(new Date(millis));
        return new Literal(lex, XSD_DATETIME);
    }

}
