package org.nsdl.mptstore.core;

import java.sql.Connection;

import java.util.Iterator;
import java.util.List;

import org.nsdl.mptstore.query.QueryException;
import org.nsdl.mptstore.query.QueryLanguage;
import org.nsdl.mptstore.query.QueryResults;

public interface DatabaseAdaptor {

    public void add(Connection conn, 
                    Iterator<List<String>> triples) 
            throws ModificationException;

    public void delete(Connection conn, 
                       Iterator<List<String>> triples) 
            throws ModificationException;

    public QueryResults query(Connection conn, 
                              QueryLanguage lang,
                              String queryText) 
            throws QueryException;

}
