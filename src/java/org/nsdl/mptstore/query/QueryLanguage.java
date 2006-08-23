package org.nsdl.mptstore.query;

public class QueryLanguage {

    public static final QueryLanguage SPO    = new QueryLanguage("spo");

    public static final QueryLanguage SPARQL = new QueryLanguage("sparql");

    private String _name;

    protected QueryLanguage(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

}
