package org.nsdl.mptstore.impl.oracle;

import java.util.ArrayList;
import java.util.List;

import org.nsdl.mptstore.core.AbstractDDLGenerator;

/**
 * A <code>DDLGenerator</code> that works with Oracle.
 * <p>
 *   The map table DDL defines pKey as a <code>NUMBER</code> value
 *   and p as a <code>VARCHAR</code> value, with indexes on each column.
 * </p>
 * <p>
 *   The relationship table DDL defines s and o
 *   as <code>VARCHAR</code> values, with indexes on each column.
 * </p>
 * <p>
 *   We only override the getDropMapTableDDL method from AbstractDDLGenerator
 *   since Oracle doesn't drop the sequence when a table is dropped.
 * </p>
 *
 * @author dgiral
 */
public class OracleDDLGenerator extends AbstractDDLGenerator {

    private static final int DEFAULT_VARCHAR_LENGTH = 512;

    private final int _varcharLength;

    /**
     * Construct a OracleDDLGenerator that uses the default maximum length (512)
     * for varchar columns that store URIs and literals.
     */
    public OracleDDLGenerator() {
        _varcharLength = DEFAULT_VARCHAR_LENGTH;
    }

    /**
     * Construct a OracleDDLGenerator that uses the specified maximum length
     * for varchar columns that store URIs and literals.
     *
     * @param varcharLength the length for all VARCHAR columns.
     */
    public OracleDDLGenerator(final int varcharLength) {
        _varcharLength = varcharLength;
    }

    /** {@inheritDoc} */
    public List<String> getCreateMapTableDDL(final String table) {

        List<String> cmds = new ArrayList<String>();

        cmds.add("CREATE TABLE " + table + " (\n"
               + "  pKey NUMBER PRIMARY KEY,\n"
               + "  p VARCHAR(" + _varcharLength + ") NOT NULL\n"
               + ")");
        cmds.add("CREATE SEQUENCE seq_" + table);
        cmds.add("CREATE TRIGGER trg_" + table + " \n"
               + "BEFORE INSERT ON " + table + " \n"
               + "FOR EACH ROW \n"
               + "BEGIN \n"
               + "  SELECT seq_" + table + ".nextval INTO :new.pKey FROM dual;"
               + "END;");
        cmds.add("CREATE INDEX " + table + "_p "
               + " on " + table + " (p)");

        return cmds;
    }

    /** {@inheritDoc} */
    public List<String> getDropMapTableDDL(final String table) {
        List<String> cmds = new ArrayList<String>();
        cmds.add("DROP TABLE " + table);
        cmds.add("DROP SEQUENCE seq_" + table);
        return cmds;
    }

    /** {@inheritDoc} */
    public List<String> getCreateSOTableDDL(final String table) {

        List<String> cmds = new ArrayList<String>();

        cmds.add("CREATE TABLE " + table + " (\n"
                + "  s VARCHAR(" + _varcharLength + ") NOT NULL,\n"
                + "  o VARCHAR(" + _varcharLength + ") NOT NULL\n"
                + ")");
        cmds.add("CREATE INDEX " + table + "_s "
               + " on " + table + " (s)");
        cmds.add("CREATE INDEX " + table + "_o "
               + " on " + table + " (o)");

        return cmds;
    }

}
