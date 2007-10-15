package org.nsdl.mptstore.impl.postgres;

import java.util.ArrayList;
import java.util.List;

import org.nsdl.mptstore.core.AbstractDDLGenerator;

/**
 * A <code>DDLGenerator</code> that works with Postgres.
 * <p>
 *   The map table DDL defines pKey as a <code>SERIAL</code> value
 *   and p as a <code>TEXT</code> value, with indexes on each column.
 * </p>
 * <p>
 *   The relationship table DDL defines s and o
 *   as <code>TEXT</code> values, with indexes on each column.
 * </p>
 * <p>
 *   Since Postgres automatically drops associated indexes
 *   and sequences when the table is dropped, the AbstractDDLGenerator
 *   implementation is used for the drop methods.
 * </p>
 *
 * @author cwilper@cs.cornell.edu
 */
public class PostgresDDLGenerator extends AbstractDDLGenerator {

    private final String[] _users;

    private final String[] _groups;

    /**
     * Construct a PostgresDDLGenerator.
     */
    public PostgresDDLGenerator() {
        _users = splitProperty("mptstore.postgres.autoGrantUsers");
        _groups = splitProperty("mptstore.postgres.autoGrantGroups");
    }

    /**
     * Construct a PostgresDDLGenerator that will grant access on new tables to
     * specified users or groups.
     *
     * @param users Users to assign privileges to when creating new tables.
     * @param groups Groups to assign privileges to when creating new tables.
     */
    public PostgresDDLGenerator(final String[] users, final String[] groups) {
        if (users == null) {
            _users = new String[0];
        } else {
            _users = users;
        }
        if (groups == null) {
            _groups = new String[0];
        } else {
            _groups = groups;
        }
    }

    private static String[] splitProperty(final String name) {
        String val = System.getProperty(name);
        if (val == null || val.trim().length() == 0) {
            return new String[0];
        } else {
            return val.trim().split(" +");
        }
    }

    private void addSelectGrants(final List<String> cmds, final String table) {
        for (String name : _users) {
            cmds.add("GRANT SELECT ON TABLE " + table + " TO " + name);
        }
        for (String name : _groups) {
            cmds.add("GRANT SELECT ON TABLE " + table + " TO GROUP " + name);
        }
    }

    /** {@inheritDoc} */
    public List<String> getCreateMapTableDDL(final String table) {

        List<String> cmds = new ArrayList<String>();

        cmds.add("CREATE TABLE " + table + " (\n"
               + "  pKey SERIAL,\n"
               + "  p TEXT NOT NULL\n"
               + ")");
        cmds.add("CREATE INDEX " + table + "_pKey "
               + " on " + table + " (pKey)");
        cmds.add("CREATE INDEX " + table + "_p "
               + " on " + table + " (p)");
        addSelectGrants(cmds, table);

        return cmds;
    }

    /** {@inheritDoc} */
    public List<String> getCreateSOTableDDL(final String table) {

        List<String> cmds = new ArrayList<String>();

        cmds.add("CREATE TABLE " + table + " (\n"
               + "  s TEXT NOT NULL,\n"
               + "  o TEXT NOT NULL\n"
               + ")");
        cmds.add("CREATE INDEX " + table + "_s "
               + " on " + table + " (s)");
        cmds.add("CREATE INDEX " + table + "_o "
               + " on " + table + " (o)");
        addSelectGrants(cmds, table);

        return cmds;
    }

}
