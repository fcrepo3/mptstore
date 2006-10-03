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

    /**
     * Construct a PostgresDDLGenerator.
     */
    public PostgresDDLGenerator() {
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

        return cmds;
    }

}
