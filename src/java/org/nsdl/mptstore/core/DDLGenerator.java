package org.nsdl.mptstore.core;

import java.util.List;

/**
 * Provides RDBMS-specific DDL string(s) for table management functions.
 */
public interface DDLGenerator {

    public List<String> getCreateMapTableDDL(String table);

    public List<String> getDropMapTableDDL(String table);

    public List<String> getCreateSOTableDDL(String table);

    public List<String> getDropSOTableDDL(String table);

}