package org.nsdl.mptstore.query;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.nsdl.mptstore.core.TableManager;

public class SPOSQLProvider implements SQLProvider {

    public static final List<String> SPO_TARGETS;

    static {
        SPO_TARGETS = new ArrayList<String>(3);
        SPO_TARGETS.add("s");
        SPO_TARGETS.add("p");
        SPO_TARGETS.add("o");
    }

    private TableManager _tableManager;
    private boolean _backslashIsEscape;
    private String _subject;
    private String _object;

    private List<String> _sql;

    public SPOSQLProvider(TableManager tableManager,
                          boolean backslashIsEscape,
                          String subject,
                          String predicate,
                          String object) {
        _tableManager = tableManager;
        _backslashIsEscape = backslashIsEscape;
        _subject = subject;
        _object = object;

        _sql = new ArrayList<String>();

        if (predicate != null) {
            addSelect(predicate);
        } else {
            Iterator<String> preds = _tableManager.getPredicates().iterator();
            while (preds.hasNext()) {
                addSelect(preds.next());
            }
        }
    }

    /**
     * If a table exists for the given predicate, add the appropriate
     * SELECT query to the list.
     */
    private void addSelect(String predicate) {

        String table = _tableManager.getTableFor(predicate);

        if (table != null) {

            StringBuffer select = new StringBuffer();

            select.append("SELECT s, ");
            select.append(quotedString(predicate));
            select.append(", o\nFROM ");
            select.append(table);

            if (_subject != null || _object != null) {
                select.append("\nWHERE ");
                if (_subject != null) {
                    select.append("s = ");
                    select.append(quotedString(_subject));
                    if (_object != null) {
                        select.append("\nAND ");
                    }
                }
                if (_object != null) {
                    select.append("o = ");
                    select.append(quotedString(_object));
                }
            }

            _sql.add(select.toString());
        }
    }

    private String quotedString(String s) {

        StringBuffer out = new StringBuffer();

        out.append('\'');

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '\'') {
                out.append("''");
            } else if (c == '\\' && _backslashIsEscape) {
                out.append("\\\\");
            } else {
                out.append(c);
            }
        }

        out.append('\'');

        return out.toString();
    }

    public List<String> getTargets() {
        return SPO_TARGETS;
    }

    public List<String> getSQL() {
        return _sql;
    }

}
