package org.nsdl.mptstore.util;

import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class DBUtil {

    /**
     * Get a long string, which could be a TEXT or CLOB type.
     * (CLOBs require special handling -- this method normalizes the reading of them)
     */
    public static String getLongString(ResultSet rs, int pos) 
            throws SQLException {
        String s = rs.getString(pos);
        if (s != null) {
            return s;
        } else {
            Clob c = rs.getClob(pos);
            return c.getSubString(1, (int) c.length());
        }
    }

    public static String quotedString(String in,
                                      boolean backslashIsEscape) {
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

}
