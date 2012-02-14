package org.nsdl.mptstore.query.lang;

/**
 * An RDF query language.
 *
 * @author cwilper@cs.cornell.edu
 */
public class QueryLanguage {

    /**
     * SPO.
     *
     * @see org.nsdl.mptstore.query.lang.spo.SPOQueryCompiler
     */
    public static final QueryLanguage SPO    = new QueryLanguage("spo");

    /**
     * SPARQL.
     *
     * @see <a href="http://www.w3.org/TR/rdf-sparql-query/">
     *      SPARQL Query Language for RDF</a>
     */
    public static final QueryLanguage SPARQL = new QueryLanguage("sparql");

    /**
     * The name of this language.
     */
    private String _name;

    /**
     * Instantiate a <code>QueryLanguage</code>.
     *
     * @param name the name.
     */
    protected QueryLanguage(final String name) {
        _name = name;
    }

    /**
     * Get the name of this language.
     *
     * @return the name.
     */
    public String getName() {
        return _name;
    }

}
