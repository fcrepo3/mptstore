package org.nsdl.mptstore.core;

import java.sql.Connection;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.nsdl.mptstore.query.QueryCompiler;
import org.nsdl.mptstore.query.QueryException;
import org.nsdl.mptstore.query.QueryLanguage;
import org.nsdl.mptstore.query.QueryResults;
import org.nsdl.mptstore.query.SPOQueryCompiler;

public class GenericDatabaseAdaptor implements DatabaseAdaptor {

    private TableManager _tableManager;

    private Map<QueryLanguage, QueryCompiler> _compilerMap;

    /**
     * Get an instance supporting the built-in query languages.
     */
    public GenericDatabaseAdaptor(TableManager tableManager) {
        _tableManager = tableManager;
        _compilerMap = new HashMap<QueryLanguage, QueryCompiler>();
        _compilerMap.put(QueryLanguage.SPO, 
                         new SPOQueryCompiler());
    }

    /**
     * Get an instance supporting the specified query languages.
     */
    public GenericDatabaseAdaptor(TableManager tableManager,
                                  Map<QueryLanguage, QueryCompiler> compilerMap) {
        _tableManager = tableManager;
        _compilerMap = compilerMap;
    }

    // Implements DatabaseAdaptor.addTriples(Connection, Iterator<List<String>>)
    public void addTriples(Connection conn, 
                           Iterator<List<String>> triples) 
            throws ModificationException {
    }

    // Implements DatabaseAdaptor.deleteTriples(Connection, Iterator<List<String>>)
    public void deleteTriples(Connection conn, 
                              Iterator<List<String>> triples) 
            throws ModificationException {
    }

    // Implements DatabaseAdaptor.query(Connection, QueryLanguage, String)
    public QueryResults query(Connection conn, 
                              QueryLanguage lang,
                              String queryText) 
            throws QueryException {
        return null;
    }

}
