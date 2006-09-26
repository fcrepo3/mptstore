package org.nsdl.mptstore.core;

import java.sql.SQLException;

import java.util.Set;

import org.nsdl.mptstore.rdf.PredicateNode;

public interface TableManager {

    public String getOrMapTableFor(PredicateNode predicate) throws SQLException;

    public String getTableFor(PredicateNode predicate);

    public PredicateNode getPredicateFor(String table);

    public Set<String> getTables();

    public Set<PredicateNode> getPredicates();

    public int dropEmptyPredicateTables() throws SQLException;

    public int dropAllPredicateTables() throws SQLException;

}
