package org.nsdl.mptstore.core;

import java.util.ArrayList;
import java.util.List;

/**
 * A partial <code>DDLGenerator</code> that implements the
 * table dropping functionality.
 *
 * Drops are implemented using a single "DROP TABLE $name" command.
 *
 * @author cwilper@cs.cornell.edu
 */
public abstract class AbstractDDLGenerator implements DDLGenerator {

    /** {@inheritDoc} */
    public List<String> getDropMapTableDDL(String table) {
        List<String> cmds = new ArrayList<String>();
        cmds.add("DROP TABLE " + table);
        return cmds;
    }

    /** {@inheritDoc} */
    public List<String> getDropSOTableDDL(String table) {
        List<String> cmds = new ArrayList<String>();
        cmds.add("DROP TABLE " + table);
        return cmds;
    }

}
