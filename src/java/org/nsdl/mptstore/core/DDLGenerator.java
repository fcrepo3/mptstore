package org.nsdl.mptstore.core;

import java.util.List;

/**
 * Provides RDBMS-specific DDL string(s) for table management functions.
 *
 * <p>
 *   The map table should have columns pKey (which holds an auto-incremented 
 *   integer) and p (which holds the actual predicate in N-Triples format).
 * </p>
 * <p>
 *   The SO (predicate) tables should each have columns s and o,
 *   for holding the subject and object strings in N-Triples format.
 * </p>
 * <h2>Note on Datatypes</h2>
 * <p>
 *   Typically the s, p, and o datatypes be declared as TEXT or CLOB in the
 *   underlying database, but they can also be defined as varchars if the 
 *   data set is known to fit within some pre-determined range.  The 
 *   type should be such that comparisons are case sensitive, 
 *   as N-Triples is a case sensitive format.
 * </p>
 * <h2>Note on Character Encoding</h2>
 * <p>
 *   Note: The database need only be capable of storing 7-bit ASCII
 *   because the RDF is stored in N-Triples format.  In fact, it will
 *   likely perform better if the database is defined as such because
 *   more data will fit in less space if characters only take up 1 byte
 *   in the database.
 * </p>
 *
 * @author cwilper@cs.cornell.edu
 */
public interface DDLGenerator {

    /**
     * Get the DDL command(s) necessary to create a map table
     * with the given name.
     *
     * @param table The map table name.
     * @return The necessary DDL.
     */
    List<String> getCreateMapTableDDL(String table);

    /**
     * Get the DDL command(s) necessary to drop a map table
     * with the given name.
     *
     * @param table The map table name.
     * @return The necessary DDL.
     */
    List<String> getDropMapTableDDL(String table);

    /**
     * Get the DDL command(s) necessary to create a subject-object
     * relationship table (aka predicate table) with the given name.
     *
     * @param table The relationship table name.
     * @return The necessary DDL.
     */
    List<String> getCreateSOTableDDL(String table);

    /**
     * Get the DDL command(s) necessary to drop a subject-object
     * relationship table (aka predicate table) with the given name.
     *
     * @param table The relationship table name.
     * @return The necessary DDL.
     */
    List<String> getDropSOTableDDL(String table);

}
