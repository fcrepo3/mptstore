package org.nsdl.mptstore.core;

import java.sql.Connection;

import java.util.Iterator;

import org.nsdl.mptstore.query.QueryException;
import org.nsdl.mptstore.query.QueryLanguage;
import org.nsdl.mptstore.query.QueryResults;
import org.nsdl.mptstore.rdf.Triple;

public interface DatabaseAdaptor {

    public void addTriples(Connection conn,
                           Iterator<Triple> triples) 
            throws ModificationException;

    public void deleteTriples(Connection conn, 
                              Iterator<Triple> triples) 
            throws ModificationException;

    public void deleteAllTriples(Connection conn)
            throws ModificationException;

    public QueryResults query(Connection conn, 
                              QueryLanguage lang,
                              int fetchSize,
                              String queryText) 
            throws QueryException;

}
