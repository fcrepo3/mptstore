package org.nsdl.mptstore.query.provider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import org.nsdl.mptstore.core.TableManager;
import org.nsdl.mptstore.query.component.TriplePattern;
import org.nsdl.mptstore.rdf.ObjectNode;
import org.nsdl.mptstore.rdf.PredicateNode;
import org.nsdl.mptstore.rdf.SubjectNode;
import org.nsdl.mptstore.util.DBUtil;

/**
 * Translates a {@link TriplePattern} into a series of SQL statements.
 *
 * @author cwilper@cs.cornell.edu.
 */
public class TriplePatternSQLProvider implements SQLProvider {

    /**
     * The Logger for this class.
     */
    private static final Logger LOG =
            Logger.getLogger(TriplePatternSQLProvider.class.getName());

    private TableManager _tableManager;

    private boolean _backslashIsEscape;

    private List<String> _targets;

    private String _subjectString;

    private String _objectString;

    private List<String> _sql;

    /**
     * Instantiate from the given values.
     *
     * @param tableManager the table manager to use for getting table names.
     * @param backslashIsEscape whether backslash should be escaped in SQL.
     * @param pattern the triple pattern.
     * @param targets the variable names to use.
     */
    public TriplePatternSQLProvider(final TableManager tableManager,
                                    final boolean backslashIsEscape,
                                    final TriplePattern pattern,
                                    final List<String> targets) {

        _tableManager = tableManager;
        _backslashIsEscape = backslashIsEscape;
        _targets = targets;

        SubjectNode subject = pattern.getSubject().getNode();
        PredicateNode predicate = pattern.getPredicate().getNode();
        ObjectNode object = pattern.getObject().getNode();

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
            Iterator<PredicateNode> preds =
                    _tableManager.getPredicates().iterator();
            while (preds.hasNext()) {
                addSelect(preds.next());
            }
        }
    }

    /**
     * If a table exists for the given predicate, add the appropriate
     * SELECT query to the list.
     *
     * @param predicate the predicate in question.
     */
    private void addSelect(final PredicateNode predicate) {

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
                    select.append(DBUtil.quotedString(_subjectString,
                            _backslashIsEscape));
                    if (_objectString != null) {
                        select.append("\nAND ");
                    }
                }
                if (_objectString != null) {
                    select.append("o = ");
                    select.append(DBUtil.quotedString(_objectString,
                            _backslashIsEscape));
                }
            }

            String sqlString = select.toString();
            LOG.debug("Generated query:\n" + sqlString);
            _sql.add(sqlString);
        }
    }

    /** {@inheritDoc} */
    public List<String> getTargets() {
        return _targets;
    }

    /** {@inheritDoc} */
    public List<String> getSQL() {
        return _sql;
    }

}
