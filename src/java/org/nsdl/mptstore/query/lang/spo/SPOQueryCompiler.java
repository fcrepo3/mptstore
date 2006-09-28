package org.nsdl.mptstore.query.lang.spo;

import java.io.IOException;
import java.io.StringReader;

import java.text.ParseException;

import java.util.ArrayList;
import java.util.List;

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
 * SPO is a simple query language that returns triples (subject, predicate,
 * object) given a single triple pattern where an asterisk in any
 * position means "any".
 *
 * <p>
 *   The grammar for SPO queries is the same as the N-Triples "triple"
 *   production, except it allows an asterisk in any node position and 
 *   is not terminated with a dot.
 * </p>
 */
public class SPOQueryCompiler implements QueryCompiler {

    private static final String _EXPECTED_AQLST = "Expected '*', '\"', '<', ' ', or TAB";
    private static final String _EXPECTED_ALST = "Expected '*', '<', ' ', or TAB";
    private static final String _EXPECTED_EOS = "Expected end of query string";
    private static final String _EXPECTED_G = "Expected '>'";
    private static final String _EXPECTED_ST = "Expected ' ' or TAB";

    private TableManager _tableManager;
    private boolean _backslashIsEscape;

    public SPOQueryCompiler(TableManager tableManager,
                            boolean backslashIsEscape) {
        _tableManager = tableManager;
        _backslashIsEscape = backslashIsEscape;
    }

    // Implements QueryCompiler.compile(String)
    public SQLProvider compile(String query) 
            throws QueryException {
        try {
            return new TriplePatternSQLProvider(
                    _tableManager, 
                    _backslashIsEscape,
                    parseTriplePattern(query));
        } catch (ParseException e) {
            throw new QuerySyntaxException("Error parsing SPO query", e);
        }
    }

    private TriplePattern parseTriplePattern(String query) 
            throws ParseException {

        StringReader reader = new StringReader(query);

        try {

            int i = 0;
            int c = reader.read();

            // start with subject
            SubjectNode subject;
            if (c == '*') {
                subject = null;
            } else {
                StringBuffer sBuf = new StringBuffer();
                while (c != '>') {
                    if (c == -1) {
                        throw new ParseException(_EXPECTED_G, i);
                    }
                    sBuf.append((char) c);
                    i++;
                    c = reader.read();
                }
                sBuf.append((char) c);
                subject = NTriplesUtil.parseSubject(sBuf.toString());
            }

            // followed by one or more whitespace
            i++;
            c = reader.read();
            if (c != ' ' && c != '\t') {
                throw new ParseException(_EXPECTED_ST, i);
            }
            while (c == ' ' || c == '\t') {
                if (c == -1) {
                    throw new ParseException(_EXPECTED_ALST, i);
                }
                i++;
                c = reader.read();
            }

            // followed by predicate
            PredicateNode predicate;
            if (c == '*') {
                predicate = null;
            } else {
                StringBuffer pBuf = new StringBuffer();
                while (c != '>') {
                    if (c == -1) {
                        throw new ParseException(_EXPECTED_G, i);
                    }
                    pBuf.append((char) c);
                    i++;
                    c = reader.read();
                }
                pBuf.append((char) c);
                try {
                    predicate = NTriplesUtil.parsePredicate(pBuf.toString());
                } catch (ParseException e) {
                    throw new ParseException(e.getMessage(), e.getErrorOffset() + i);
                }
            }

            // followed by one or more whitespace
            i++;
            c = reader.read();
            if (c != ' ' && c != '\t') {
                throw new ParseException(_EXPECTED_ST, i);
            }
            while (c == ' ' || c == '\t') {
                if (c == -1) {
                    throw new ParseException(_EXPECTED_AQLST, i);
                }
                i++;
                c = reader.read();
            }
            // followed by object
            ObjectNode o;
            if (c == '*') {
                o = null;
                if (reader.read() != -1) {
                    throw new ParseException(_EXPECTED_EOS, i + 1);
                }
            } else {
                try {
                    o = NTriplesUtil.parseObject(query.substring(i));
                } catch (ParseException e) {
                    throw new ParseException(e.getMessage(), e.getErrorOffset() + i);
                }
            }
            reader.close();

            // construct the pattern from the components and return

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
            if (o != null) {
                oPattern = new BasicNodePattern<ObjectNode>(o);
            } else {
                oPattern = new BasicNodePattern<ObjectNode>("o");
            }

            return new BasicTriplePattern(sPattern, pPattern, oPattern);

        } catch (IOException e) {
            // should not happen -- we're using a StringReader
            throw new RuntimeException("Unexpected IO error", e);
        }

    }

}
