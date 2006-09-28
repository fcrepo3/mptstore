package org.nsdl.mptstore.query.provider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import org.nsdl.mptstore.core.TableManager;
import org.nsdl.mptstore.rdf.ObjectNode;
import org.nsdl.mptstore.rdf.PredicateNode;
import org.nsdl.mptstore.rdf.SubjectNode;
import org.nsdl.mptstore.rdf.Triple;
import org.nsdl.mptstore.util.DBUtil;

public class TriplePatternSQLProvider implements SQLProvider {

    private static final Logger _LOG = Logger.getLogger(TriplePatternSQLProvider.class.getName());

    public static final List<String> SPO_TARGETS;

    static {
        SPO_TARGETS = new ArrayList<String>(3);
        SPO_TARGETS.add("s");
        SPO_TARGETS.add("p");
        SPO_TARGETS.add("o");
    }

    private TableManager _tableManager;
    private boolean _backslashIsEscape;
    private String _subjectString;
    private String _objectString;

    private List<String> _sql;

    public TriplePatternSQLProvider(TableManager tableManager,
                          boolean backslashIsEscape,
                          Triple pattern) {

        _tableManager = tableManager;
        _backslashIsEscape = backslashIsEscape;

        SubjectNode subject = pattern.getSubject();
        PredicateNode predicate = pattern.getPredicate();
        ObjectNode object = pattern.getObject();

        if (subject != null) {
            _subjectString = subject.toString();
        }

        if (object != null) {
            _objectString = object.toString();
        }

        _sql = new ArrayList<String>();

        if (predicate != null) {
            addSelect(predicate);
        } else {
            Iterator<PredicateNode> preds = _tableManager.getPredicates().iterator();
            while (preds.hasNext()) {
                addSelect(preds.next());
            }
        }
    }

    /**
     * If a table exists for the given predicate, add the appropriate
     * SELECT query to the list.
     */
    private void addSelect(PredicateNode predicate) {

        String table = _tableManager.getTableFor(predicate);

        if (table != null) {

            StringBuffer select = new StringBuffer();

            select.append("SELECT s, ");
            select.append(DBUtil.quotedString(predicate.toString(), 
                                             _backslashIsEscape));
            select.append(", o\nFROM ");
            select.append(table);

            if (_subjectString != null || _objectString != null) {
                select.append("\nWHERE ");
                if (_subjectString != null) {
                    select.append("s = ");
                    select.append(DBUtil.quotedString(_subjectString, _backslashIsEscape));
                    if (_objectString != null) {
                        select.append("\nAND ");
                    }
                }
                if (_objectString != null) {
                    select.append("o = ");
                    select.append(DBUtil.quotedString(_objectString, _backslashIsEscape));
                }
            }

            String sqlString = select.toString();
            _LOG.info("Generated query:\n" + sqlString);
            _sql.add(sqlString);
        }
    }

    public List<String> getTargets() {
        return SPO_TARGETS;
    }

    public List<String> getSQL() {
        return _sql;
    }

}
