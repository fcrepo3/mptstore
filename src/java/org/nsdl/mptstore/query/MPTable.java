package org.nsdl.mptstore.query;

/** Mapped Predicate Table
 * <p>
 * Represents a specific mapping of a predicate to a table.  This mapped
 * table has both a name and an alias.  The table name is determined by the
 * predicate mapping, but the alias is arbitrary.
 * </p>
 * 
 * @author birkland
 *
 */
public class MPTable {
    private final String alias;
    private final String name;

    public MPTable(String name, String alias) {
        this.alias = alias;
        this.name = name;
    }
    public String alias() {
        return alias;
    }
    public String name() {
        return name;
    }
}
