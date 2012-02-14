package org.nsdl.mptstore.impl.h2;

import java.util.ArrayList;
import java.util.List;

import org.nsdl.mptstore.core.AbstractDDLGenerator;
/**
 * A <code>DDLGenerator</code> that works with H2.
 * <p>
 *   The map table DDL defines pKey as a <code>INT AUTO_INCREMENT</code> value
 *   and p as an unbound <code>VARCHAR</code> value, with indexes on each
 *   column.
 * </p>
 * <p>
 *   The relationship table DDL defines s and o
 *   as <code>VARCHAR</code> values, with indexes on each column.
 * </p>
 * <p>
 *   In H2, there is no pre-defined limit on the length of a
 *   <code>VARCHAR</code>, unless one is provided.  <code>VARCHAR</code>s
 *   must, however, be able to fit entirely in memory.  As a result, this
 *   DDLGenerator does not specify an artificial limit to their length.
 * </p>
 *
 * @author birkland
 */
public class H2DDLGenerator extends AbstractDDLGenerator {

    /** Create an H2 DDLGenerator with no length limit textual values. */
    public H2DDLGenerator() {
    }

    /** {@inheritDoc} */
    public List<String> getCreateMapTableDDL(final String table) {

        List<String> cmds = new ArrayList<String>();

        cmds.add("CREATE TABLE " + table + " (\n"
               + "  pKey INT AUTO_INCREMENT,\n"
               + "  p VARCHAR NOT NULL\n"
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
               + "  s VARCHAR NOT NULL,\n"
               + "  o VARCHAR NOT NULL\n"
               + ")");
        cmds.add("CREATE INDEX " + table + "_s "
               + " on " + table + " (s)");
        cmds.add("CREATE INDEX " + table + "_o "
               + " on " + table + " (o)");

        return cmds;
    }

}
