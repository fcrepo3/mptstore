package org.nsdl.mptstore.util;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

/**
 * Database-related utilities.
 *
 * @author cwilper@cs.cornell.edu
 */
public abstract class DBUtil {

    /**
     * Logger for this class.
     */
    private static final Logger LOG =
            Logger.getLogger(DBUtil.class.getName());

    private DBUtil() { }

    /**
     * Get a long string, which could be a TEXT or CLOB type.
     *
     * CLOBs require special handling.  This method normalizes the
     * reading of them.
     *
     * @param rs The ResultSet whose current row contains the desired value.
     * @param pos The position (column) of the value in the current row.
     * @return The desired string, or <code>null</code> if the value in the
     *         ResultSet is null.
     * @throws SQLException if there is an database error accessing the value
     *         from the ResultSet.
     */
    public static String getLongString(final ResultSet rs,
                                       final int pos)
            throws SQLException {
        String s = rs.getString(pos);
        if (s != null) {
            return s;
        } else {
            Clob c = rs.getClob(pos);

            if (c == null) {
                return null;
            } else {
                return c.getSubString(1, (int) c.length());
            }
        }
    }

    /**
     * Provide a single-quoted, properly escaped String for the given
     * value to be used in a SQL statement.
     *
     * Apostrophes will always be escaped as ''.  Backslashes will
     * be escaped as \\ if backslashIsEscape is given as <code>true</code>.
     *
     * @param in The input value.
     * @param backslashIsEscape Whether backslash characters are treated
     *        as escape characters by the underlying database implementation,
     *        and thus need to be escaped themselves.
     * @return the escaped string.
     */
    public static String quotedString(final String in,
                                      final boolean backslashIsEscape) {
        StringBuffer out = new StringBuffer();
        out.append('\'');
        for (int i = 0; i < in.length(); i++) {
            char c = in.charAt(i);
            if (c == '\'') {
                out.append("''");                           //  ' ==> ''
            } else if (backslashIsEscape && c == '\\') {
                out.append("\\\\");                         //  \ ==> \\
            } else {
                out.append(c);
            }
        }
        out.append('\'');
        return out.toString();
    }

    /**
     * Ensure the given connection is in auto-commit mode (default)
     * and close/release it.
     *
     * Any errors encountered will be logged.
     *
     * @param conn the connection.
     */
    public static void release(final Connection conn) {
        try {
            if (!conn.getAutoCommit()) {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            LOG.warn("Error setting autocommit", e);
        }
        try {
            conn.close();
        } catch (SQLException e) {
            LOG.warn("Error closing/releasing connection", e);
        }
    }

}
