package org.nsdl.mptstore.core;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.Set;

public interface TableManager {

    public String getOrMapTableFor(String predicate) throws SQLException;

    public String getTableFor(String predicate);

    public String getPredicateFor(String table);

    public Set<String> getTables();

    public Set<String> getPredicates();

}
