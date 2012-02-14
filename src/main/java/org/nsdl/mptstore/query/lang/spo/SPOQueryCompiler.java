package org.nsdl.mptstore.query.lang.spo;

import java.io.IOException;
import java.io.StringReader;

import java.text.ParseException;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import org.nsdl.mptstore.core.TableManager;
import org.nsdl.mptstore.query.QueryException;
import org.nsdl.mptstore.query.component.BasicNodePattern;
import org.nsdl.mptstore.query.component.BasicTriplePattern;
import org.nsdl.mptstore.query.component.NodePattern;
import org.nsdl.mptstore.query.component.TriplePattern;
import org.nsdl.mptstore.query.lang.QueryCompiler;
import org.nsdl.mptstore.query.lang.QuerySyntaxException;
import org.nsdl.mptstore.query.provider.SQLProvider;
import org.nsdl.mptstore.query.provider.TriplePatternSQLProvider;
import org.nsdl.mptstore.rdf.ObjectNode;
import org.nsdl.mptstore.rdf.PredicateNode;
import org.nsdl.mptstore.rdf.SubjectNode;
import org.nsdl.mptstore.util.NTriplesUtil;

/**
 * Compiler for SPO queries.
 *
 * <p>
 *   SPO is a simple query language that returns triples (subject, predicate,
 *   object) given a single triple pattern where an asterisk in any
 *   position acts as a wildcard.
 * </p>
 * <p>
 *   The grammar for SPO queries is the same as the N-Triples "triple"
 *   production, except it allows an asterisk in any node position and
 *   is not terminated with a dot.
 * </p>
 *
 * @see <a href="http://www.w3.org/TR/rdf-testcases/#triple">
 *      N-Triples "triple" syntax specification</a>
 * @author cwilper@cs.cornell.edu
 */
public class SPOQueryCompiler implements QueryCompiler {

    /**
     * The targets are always "s", "p", "o".
     */
    public static final List<String> SPO_TARGETS;

    /** Logger for this class. */
    private static final Logger LOG = Logger.getLogger(
            SPOQueryCompiler.class.getName());

    static {
        SPO_TARGETS = new ArrayList<String>();
        SPO_TARGETS.add("s");
        SPO_TARGETS.add("p");
        SPO_TARGETS.add("o");
    }

    private static final String EXPECTED_AQLST = "Expected '*', '\"',"
            + " '<', ' ', or TAB";
    private static final String EXPECTED_ALST = "Expected '*', '<',"
            + " ' ', or TAB";
    private static final String EXPECTED_EOS = "Expected end of query string";
    private static final String EXPECTED_G = "Expected '>'";
    private static final String EXPECTED_ST = "Expected ' ' or TAB";

    /**
     * The table manager used to look up table names for predicates.
     */
    private TableManager _tableManager;

    /**
     * Whether the backslash character should be escaped in the output SQL.
     */
    private boolean _backslashIsEscape;

    /**
     * Instantiate an SPOQueryCompiler.
     *
     * @param tableManager The table manager to be used to look up table names
     *                     for predicates.
     * @param backslashIsEscape Whether the backslash character should be
     *                          escaped in the output SQL.
     */
    public SPOQueryCompiler(final TableManager tableManager,
                            final boolean backslashIsEscape) {
        _tableManager = tableManager;
        _backslashIsEscape = backslashIsEscape;
    }

    /** {@inheritDoc} */
    public SQLProvider compile(final String query)
            throws QueryException {
        try {
            LOG.info("Compiling query: " + query);
            return new TriplePatternSQLProvider(
                    _tableManager,
                    _backslashIsEscape,
                    parseTriplePattern(query),
                    SPO_TARGETS);
        } catch (ParseException e) {
            throw new QuerySyntaxException("Error parsing SPO query", e);
        }
    }

    /**
     * Translate an SPO query string into a single <code>TriplePattern</code>.
     *
     * @param query the SPO query.
     * @return TriplePattern the triple pattern.
     * @throws ParseException if the query is malformed.
     */
    private TriplePattern parseTriplePattern(final String query)
            throws ParseException {

        StringReader reader = new StringReader(query);

        try {

            int i = 0;

            // subject
            SubjectNode subject;
            int c = reader.read();
            if (c == '*') {
                subject = null;
                i++;
            } else {
                reader.reset();
                String sString = NTriplesUtil.consumeURIReference(reader, i);
                subject = NTriplesUtil.parseSubject(sString);
                i += sString.length();
            }
            if (LOG.isTraceEnabled()) {
                LOG.trace("Parsed subject pattern as " + subject);
            }

            // whitespace
            i += NTriplesUtil.consumeWhitespace(reader, i);

            // predicate
            reader.mark(Integer.MAX_VALUE);
            PredicateNode predicate;
            c = reader.read();
            if (c == '*') {
                predicate = null;
                i++;
            } else {
                reader.reset();
                String pString = NTriplesUtil.consumeURIReference(reader, i);
                try {
                    predicate = NTriplesUtil.parsePredicate(pString);
                } catch (ParseException e) {
                    throw new ParseException(e.getMessage(),
                            e.getErrorOffset() + i);
                }
                i += pString.length();
            }
            if (LOG.isTraceEnabled()) {
                LOG.trace("Parsed predicate pattern as " + predicate);
            }

            // whitespace
            i += NTriplesUtil.consumeWhitespace(reader, i);

            // object
            ObjectNode object;
            c = reader.read();
            if (c == '*') {
                object = null;
                if (reader.read() != -1) {
                    throw new ParseException(EXPECTED_EOS, i + 1);
                }
            } else {
                try {
                    object = NTriplesUtil.parseObject(query.substring(i));
                } catch (ParseException e) {
                    throw new ParseException(e.getMessage(),
                            e.getErrorOffset() + i);
                }
            }
            if (LOG.isTraceEnabled()) {
                LOG.trace("Parsed object pattern as " + object);
            }
            reader.close();

            // triple pattern
            return getTriplePattern(subject, predicate, object);

        } catch (IOException e) {
            // should not happen -- we're using a StringReader
            throw new RuntimeException("Unexpected IO error", e);
        }

    }

    /**
     * Get a <code>TriplePattern</code> from the given components.
     *
     * @param subject the subject, or <code>null</code> if unbound.
     * @param predicate the predicate, or <code>null</code> if unbound.
     * @param object the object, or <code>null</code> if unbound.
     * @return the triple pattern.
     */
    private static TriplePattern getTriplePattern(final SubjectNode subject,
                                                  final PredicateNode predicate,
                                                  final ObjectNode object) {
        NodePattern<SubjectNode> sPattern;
        if (subject != null) {
            sPattern = new BasicNodePattern<SubjectNode>(subject);
        } else {
            sPattern = new BasicNodePattern<SubjectNode>("s");
        }

        NodePattern<PredicateNode> pPattern;
        if (predicate != null) {
            pPattern = new BasicNodePattern<PredicateNode>(predicate);
        } else {
            pPattern = new BasicNodePattern<PredicateNode>("p");
        }

        NodePattern<ObjectNode> oPattern;
        if (object != null) {
            oPattern = new BasicNodePattern<ObjectNode>(object);
        } else {
            oPattern = new BasicNodePattern<ObjectNode>("o");
        }

        return new BasicTriplePattern(sPattern, pPattern, oPattern);
    }

}
