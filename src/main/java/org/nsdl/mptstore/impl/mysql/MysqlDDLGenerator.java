
package org.nsdl.mptstore.impl.mysql;

import java.util.ArrayList;
import java.util.List;

import org.nsdl.mptstore.core.AbstractDDLGenerator;

/** DDLGenerator that works with Mysql.
 * <p>
 *   The map table DDL defines pKey as a <code>AUTO INCREMENT</code> value
 *   and p as a <code>VARCHAR(N)</code> value, with indexes on each column.
 *   The default length N is 255, but this can be modified by setting a system
 *   property or as a constructor argument.
 * </p>
 * <p>
 *   The relationship table DDL defines s and o
 *   as <code>VARCHAR(N)</code> values, with indexes on each column.
 *   The default length N is 255, but this can be modified by setting a system
 *   property or as a constructor argument.
 * </p>
 * <p>
 *   The following system properties, if set, modify the default behaviour:
 *   <dl>
 *    <dt>mptstore.mysql.length</dt>
 *    <dd>
 *     Use this to set the specified varchar length when creating map or
 *     predicate tables.  Default is 255, but mysql versions greater than
 *     5.0.3 can accept up to 65536.
 *     Example: <code>-Dmptstore.mysql.length=1024</code>
 *    </dd>
 *
 *    <dt>mptstore.mysql.engine</dt>
 *    <dd>
 *     Use this to set the default storage engine for newly created tables.
 *     By default, this is unspecified, so mysql is free to choose based on
 *     its own policy.
 *     Example: <code>-Dmptstore.mysql.engine=innodb</code>
 *    </dd>
 *   </dl>
 * </p>
 *
 * @author birkland
 */
public class MysqlDDLGenerator
        extends AbstractDDLGenerator {

    private static final String DEFAULT_VARCHAR_LENGTH = "255";

    /** Property name for specifying default varchar length. */
    public static final String PROP_TEXT_LENGTH = "mptstore.mysql.length";

    /** Property name for specifying mysql storage engine. */
    public static final String PROP_STORAGE_ENGINE = "mptstore.mysql.engine";

    private final int _length;

    /**
     * Construct a MysqlDDLGenerator that uses the default maximum length (255)
     * for varchar columns that store URIs and literals.
     */
    public MysqlDDLGenerator() {
        _length =
                new Integer(System.getProperty(PROP_TEXT_LENGTH,
                                               DEFAULT_VARCHAR_LENGTH));
    }

    /**
     * Construct a DerbyDDLGenerator that uses a given maximum length
     * for varchar columns that store URIs and literals.  For mysql versions
     * newer than 5.0.3, the maximum is 65535.
     *
     * @param length default varchar length.
     */
    public MysqlDDLGenerator(final int length) {
        _length = length;
    }

    /** {@inheritDoc} */
    public List<String> getCreateMapTableDDL(final String table) {

        List<String> cmds = new ArrayList<String>();
        cmds.add("CREATE TABLE " + table + " (\n"
                + "  pKey INT UNIQUE NOT NULL AUTO_INCREMENT,\n"
                + "  p VARCHAR(" + _length + ") UNIQUE NOT NULL\n"
                + ") " + getEngine());
        return cmds;
    }

    /** {@inheritDoc} */
    public List<String> getCreateSOTableDDL(final String table) {

        List<String> cmds = new ArrayList<String>();

        cmds.add("CREATE TABLE " + table + " (\n"
                + "  s VARCHAR(" + _length + ") NOT NULL,\n"
                + "  o VARCHAR(" + _length + ") NOT NULL,\n"
                + "  INDEX " + table + "_s (s),\n"
                + "  INDEX " + table + "_o (o)\n"
                + ")" + getEngine());
        return cmds;
    }

    private String getEngine() {
        String engine = "";
        if (System.getProperty(PROP_STORAGE_ENGINE) != null) {
            engine = "ENGINE " + System.getProperty(PROP_STORAGE_ENGINE);
        }

        return engine;
    }
}