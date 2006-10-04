package org.nsdl.mptstore.impl.derby;

import java.util.ArrayList;
import java.util.List;

import org.nsdl.mptstore.core.AbstractDDLGenerator;

/**
 * A <code>DDLGenerator</code> that works with Derby.
 * <p>
 *   The map table DDL defines pKey as a <code>TODO:</code> value
 *   and p as a <code>CLOB</code> value, with indexes on each column.
 * </p>
 * <p>
 *   The relationship table DDL defines s and o
 *   as <code>CLOB</code> values, with indexes on each column.
 * </p>
 * <p>
 *   TODO: Does Derby automatically drop db indexes and sequences?
 *         If not, we have to do something special for the drop methods.
 * </p>
 *
 * @author cwilper@cs.cornell.edu
 */
public class DerbyDDLGenerator extends AbstractDDLGenerator {

    /**
     * Construct a DerbyDDLGenerator.
     */
    public DerbyDDLGenerator() {
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
