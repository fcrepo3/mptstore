package org.nsdl.mptstore.query.component;

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
        if (name == null || alias == null) {
            throw new IllegalArgumentException("Cannot create a table without a name or alias. " +
                    "Given " + name + " as name, " + alias + " as alias\n");
        }
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
