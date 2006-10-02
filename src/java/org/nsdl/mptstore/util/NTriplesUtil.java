package org.nsdl.mptstore.util;

import java.io.IOException;
import java.io.StringReader;

import java.net.URISyntaxException;

import java.text.ParseException;

import org.nsdl.mptstore.rdf.Literal;
import org.nsdl.mptstore.rdf.Node;
import org.nsdl.mptstore.rdf.ObjectNode;
import org.nsdl.mptstore.rdf.PredicateNode;
import org.nsdl.mptstore.rdf.SubjectNode;
import org.nsdl.mptstore.rdf.Triple;
import org.nsdl.mptstore.rdf.URIReference;

/**
 * Utility for parsing and outputting strings in N-Triples format.
 *
 * @author cwilper@cs.cornell.edu
 */
public abstract class NTriplesUtil {

    private static final String EXPECTED_ABS_URI = "Expected absolute URI";
    private static final String EXPECTED_ACE = "Expected '@', '^', or EOF";
    private static final String EXPECTED_C = "Expected '^'";
    private static final String EXPECTED_G = "Expected '>'";
    private static final String EXPECTED_L = "Expected '<'";
    private static final String EXPECTED_LST = "Expected '<', ' ', or TAB";
    private static final String EXPECTED_PST = "Expected '.', ' ', or TAB";
    private static final String EXPECTED_Q = "Expected '\"'";
    private static final String EXPECTED_QL = "Expected '\"' or '<'";
    private static final String EXPECTED_QLST = "Expected '\"', '<', ' ', or TAB";
    private static final String EXPECTED_ST = "Expected ' ' or TAB";
    private static final String NON_ASCII_CHAR = "Non-ASCII character";
    private static final String UNESCAPED_BACKSLASH = "Unescaped backslash";
    private static final String ILLEGAL_ESCAPE = "Illegal Unicode escape sequence";
    private static final String INCOMPLETE_ESCAPE = "Incomplete Unicode escape sequence";
    private static final String UNESCAPED_CR = "Unescaped carriage return";
    private static final String UNESCAPED_LF = "Unescaped linefeed";
    private static final String UNESCAPED_TAB = "Unescaped tab";

    /**
     * Parse an RDF triple in N-Triples format.
     *
     * @param ntTriple the input string.
     * @return the parsed triple.
     * @throws ParseException if the input syntax is incorrect
     * @see <a href="http://www.w3.org/TR/rdf-testcases/#triple">
     *        N-Triples triple syntax</a>
     */
    public static Triple parseTriple(String ntTriple) 
            throws ParseException {

        StringReader reader = new StringReader(ntTriple);

        try {

            int i = 0;
            int c = reader.read();

            // start with subject
            StringBuffer sBuf = new StringBuffer();
            while (c != '>') {
                if (c == -1) {
                    throw new ParseException(EXPECTED_G, i);
                }
                sBuf.append((char) c);
                i++;
                c = reader.read();
            }
            sBuf.append((char) c);
            SubjectNode subject = parseSubject(sBuf.toString());

            // followed by one or more whitespace
            i++;
            c = reader.read();
            if (c != ' ' && c != '\t') {
                throw new ParseException(EXPECTED_ST, i);
            }
            while (c == ' ' || c == '\t') {
                if (c == -1) {
                    throw new ParseException(EXPECTED_LST, i);
                }
                i++;
                c = reader.read();
            }

            // followed by predicate
            StringBuffer pBuf = new StringBuffer();
            while (c != '>') {
                if (c == -1) {
                    throw new ParseException(EXPECTED_G, i);
                }
                pBuf.append((char) c);
                i++;
                c = reader.read();
            }
            pBuf.append((char) c);
            PredicateNode predicate;
            try {
                predicate = parsePredicate(pBuf.toString());
            } catch (ParseException e) {
                throw new ParseException(e.getMessage(), e.getErrorOffset() + i);
            }

            // followed by one or more whitespace
            i++;
            c = reader.read();
            if (c != ' ' && c != '\t') {
                throw new ParseException(EXPECTED_ST, i);
            }
            while (c == ' ' || c == '\t') {
                if (c == -1) {
                    throw new ParseException(EXPECTED_QLST, i);
                }
                i++;
                c = reader.read();
            }
            reader.close();

            // followed by object
            int j = ntTriple.length() - 1;

            char ch = ntTriple.charAt(j);
            while (ch != '.') {
                if (ch != '\t' && ch != ' ') {
                    throw new ParseException(EXPECTED_PST, j);
                }
                j--;
                if (j < i) {
                    throw new ParseException(EXPECTED_QLST, j);
                }
                ch = ntTriple.charAt(j);
            }

            j--;
            if (j < i) {
                throw new ParseException(EXPECTED_QLST, j);
            }
            ch = ntTriple.charAt(j);
            while (ch == ' ' || ch == '\t') {
                j--;
                if (j < i) {
                    throw new ParseException(EXPECTED_QLST, j);
                }
                ch = ntTriple.charAt(j);
            }

            String oString = ntTriple.substring(i, j + 1);
            ObjectNode o;
            try {
                o = parseObject(oString);
            } catch (ParseException e) {
                throw new ParseException(e.getMessage(), e.getErrorOffset() + i);
            }

            return new Triple(subject, predicate, o);

        } catch (IOException e) {
            // should not happen -- we're using a StringReader
            throw new RuntimeException("Unexpected IO error", e);
        }

    }

    /** 
     * Parse an RDF subject in N-Triples format.
     *
     * @param ntSubject the input string.
     * @return the parsed subject.
     * @throws ParseException if the input syntax is incorrect
     * @see <a href="http://www.w3.org/TR/rdf-testcases/#subject">
     *        N-Triples subject syntax</a>
     */
    public static SubjectNode parseSubject(String ntSubject)
            throws ParseException {

        return parseURIReference(ntSubject);
    }

    /** 
     * Parse an RDF predicate in N-Triples format.
     *
     * @param ntPredicate the input string.
     * @return the parsed predicate.
     * @throws ParseException if the input syntax is incorrect
     * @see <a href="http://www.w3.org/TR/rdf-testcases/#predicate">
     *        N-Triples predicate syntax</a>
     */
    public static PredicateNode parsePredicate(String ntPredicate)
            throws ParseException {

        return parseURIReference(ntPredicate);
    }

    /** 
     * Parse an RDF object in N-Triples format.
     *
     * @param ntObject the input string.
     * @return the parsed predicate.
     * @throws ParseException if the input syntax is incorrect
     * @see <a href="http://www.w3.org/TR/rdf-testcases/#object">
     *        N-Triples object syntax</a>
     */
    public static ObjectNode parseObject(String ntObject)
            throws ParseException {

        return (ObjectNode) parseNode(ntObject);
    }

    /**
     * Parse an RDF node in N-Triples format.
     *
     * @param ntNode the input string.
     * @return the parsed node.
     * @throws ParseException if the input syntax is incorrect
     */
    public static Node parseNode(String ntNode) 
            throws ParseException {

        char first = ntNode.charAt(0);

        if (first == '"') {
            return parseLiteral(ntNode);
        } else if (first == '<') {
            return parseURIReference(ntNode);
        } else {
            throw new ParseException(EXPECTED_QL, 0);
        }
    }

    /**
     * Parse an RDF literal in N-Triples format.
     *
     * @param s the input string.
     * @return the parsed literal.
     * @throws ParseException if the input syntax is incorrect
     */
    public static Literal parseLiteral(String s)
            throws ParseException {

        StringReader reader = new StringReader(s);

        try {

            int first = reader.read();

            if (first != '"') {
                throw new ParseException(EXPECTED_Q, 0);
            }

            StringBuffer escaped = new StringBuffer();
    
            int c = reader.read();
            int i = 1;

            while (c != '"') {

                if (c == -1) {
                    throw new ParseException(EXPECTED_Q, i);
                }
                escaped.append((char) c);

                if (c == '\\') {
                    c = reader.read(); i++;
                    if (c == -1) {
                        throw new ParseException(EXPECTED_Q, i);
                    }
                    escaped.append((char) c);
                } else if (c == '\r') {
                    throw new ParseException(UNESCAPED_LF, i);
                } else if (c == '\n') {
                    throw new ParseException(UNESCAPED_CR, i);
                } else if (c == '\t') {
                    throw new ParseException(UNESCAPED_TAB, i);
                }

                c = reader.read(); i++;
            }

            String value;
            try {
                value = unescapeLiteralValue(escaped.toString());
            } catch (ParseException e) {
                throw new ParseException(e.getMessage(),
                                         e.getErrorOffset() + 1);
            }

            // c == '"', read next char
            c = reader.read(); i++;

            if (c == '@') {
                try {
                    return new Literal(value, s.substring(i + 1));
                } catch (ParseException e) {
                    throw new ParseException(e.getMessage(), 
                                             e.getErrorOffset() + i + 1);
                }
            } else if (c == '^') {
                c = reader.read(); i++;
                if (c != '^') {
                    throw new ParseException(EXPECTED_C, i);
                }
                try {
                    URIReference datatype = parseURIReference(
                            s.substring(i + 1));
                    return new Literal(value, datatype);
                } catch (ParseException e) {
                    throw new ParseException(e.getMessage(), 
                                             e.getErrorOffset() + i);
                }
            } else if (c == -1) {
                return new Literal(value);
            } else {
                throw new ParseException(EXPECTED_ACE, i);
            }

        } catch (IOException e) {
            // should not happen -- we're using a StringReader
            throw new RuntimeException("Unexpected IO error", e);
        }
    }

    /**
     * Parse an RDF URI reference in N-Triples format.
     *
     * @param s the input string.
     * @return the parsed URI reference.
     * @throws ParseException if the input syntax is incorrect
     */
    public static URIReference parseURIReference(String s)
            throws ParseException {

        char first = s.charAt(0);

        if (first != '<') {
            throw new ParseException(EXPECTED_L, 0);
        }

        char last = s.charAt(s.length() - 1);
        if (last != '>') {
            throw new ParseException(EXPECTED_G, s.length() - 1);
        }

        try {
            return new URIReference(s.substring(1, s.length() - 1));
        } catch (URISyntaxException e) {
            throw new ParseException(EXPECTED_ABS_URI, 1);
        }
    }

    /**
     * Unescape an N-Triples-escaped string.
     *
     * <ul>
     *   <li> All input characters are validated to be 7-bit ASCII.</li>
     *   <li> Unicode escapes (&#x5C;uxxxx and &#x5C;Uxxxxxxxx) are validated 
     *        to be complete and legal, and are restored to the value indicated 
     *        by the hexadecimal argument.</li>
     *   <li> Backslash-escaped values (\t, \r, \n, \", and \\) are restored 
     *        to their original form (tab, carriage return, linefeed, quote,
     *        and backslash, respectively).</li>
     * </ul>
     *
     * @param s The input string.
     * @return The unescaped string.
     * @throws ParseException if the input syntax is incorrect
     */
    public static String unescapeLiteralValue(String s)
            throws ParseException {

        // verify ascii input
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c > 127) {
                throw new ParseException(NON_ASCII_CHAR, i);
            }
        }

        int backslashPos = s.indexOf('\\');

        // return early if no escapes
        if (backslashPos == -1) {
            return s;
        }

        int i = 0;
        int len = s.length();
        StringBuffer buf = new StringBuffer(len);

        // unescape all
        while (backslashPos != -1) {
            buf.append(s.substring(i, backslashPos));

            if (backslashPos + 1 >= len) {
                throw new ParseException(UNESCAPED_BACKSLASH, i);
            }

            char c = s.charAt(backslashPos + 1);

            if (c == 't') {
                buf.append('\t');
                i = backslashPos + 2;
            } else if (c == 'r') {
                buf.append('\r');
                i = backslashPos + 2;
            } else if (c == 'n') {
                buf.append('\n');
                i = backslashPos + 2;
            } else if (c == '"') {
                buf.append('"');
                i = backslashPos + 2;
            } else if (c == '\\') {
                buf.append('\\');
                i = backslashPos + 2;
            } else if (c == 'u') {
                if (backslashPos + 5 >= len) {
                    throw new ParseException(INCOMPLETE_ESCAPE, i);
                }
                String xx = s.substring(backslashPos + 2, backslashPos + 6);
                try {
                    c = (char)Integer.parseInt(xx, 16);
                    buf.append( (char)c );
                    i = backslashPos + 6;
                } catch (NumberFormatException e) {
                    throw new ParseException(ILLEGAL_ESCAPE, i);
                }
            } else if (c == 'U') {
                if (backslashPos + 9 >= len) {
                    throw new ParseException(INCOMPLETE_ESCAPE, i);
                }
                String xx = s.substring(backslashPos + 2, backslashPos + 10);
                try {
                    c = (char)Integer.parseInt(xx, 16);
                    buf.append( (char)c );
                    i = backslashPos + 10;
                } catch (NumberFormatException e) {
                    throw new ParseException(ILLEGAL_ESCAPE, i);
                }
            } else {
                throw new ParseException(UNESCAPED_BACKSLASH, i);
            }

            backslashPos = s.indexOf('\\', i);
        }
        buf.append(s.substring(i));

        return buf.toString();
    }

    /**
     * Escape a string to N-Triples literal format.
     *
     * <ul>
     *   <li> Unicode escaping (&#x5C;uxxxx or &#x5C;Uxxxxxxxx, as
     *        appropriate) will be used for all characters in the 
     *        following ranges:
     *        <ul>
     *          <li> 0x0 through 0x8</li>
     *          <li> 0xB through 0xC</li>
     *          <li> 0xE through 0x1F</li>
     *          <li> 0x7F through 0xFFFF</li>
     *          <li> 0x10000 through 0x10FFFF</li>
     *        </ul>
     *   <li> Backslash escaping will be used for double quote (\"), 
     *        backslash (\\), line feed (\n), carriage return (\r), 
     *        and tab (\t) characters.</li>
     *   <li> All other characters will be represented as-is.</li>
     * </ul>
     *
     * @param s The input string.
     * @return The escaped string.
     */ 
    public static String escapeLiteralValue(String s) {

        int len = s.length();
        StringBuffer out = new StringBuffer(len * 2);

        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            int cNum = (int)c;
            if (c == '\\') {
                out.append("\\\\");
            } else if (c == '"') {
                out.append("\\\"");
            } else if (c == '\n') {
                out.append("\\n");
            } else if (c == '\r') {
                out.append("\\r");
            } else if (c == '\t') {
                out.append("\\t");
            } else if (
                    cNum >= 0x0 && cNum <= 0x8 ||
                    cNum == 0xB || cNum == 0xC ||
                    cNum >= 0xE && cNum <= 0x1F ||
                    cNum >= 0x7F && cNum <= 0xFFFF) {
                out.append("\\u");
                out.append(hexString(cNum, 4));
            } else if (cNum >= 0x10000 && cNum <= 0x10FFFF) {
                out.append("\\U");
                out.append(hexString(cNum, 8));
            } else {
                out.append(c);
            }
        }

        return out.toString();
    }

    /**
     * Get an uppercase hex string of the specified length, 
     * representing the given number.
     *
     * @param num The number to represent.
     * @param len The desired length of the output.
     * @return The uppercase hex string.
     */
    private static String hexString(int num, int len) {
        StringBuffer out = new StringBuffer(len);
        String hex = Integer.toHexString(num).toUpperCase();
        int n = len - hex.length();
        for (int i = 0; i < n; i++) {
            out.append('0');
        }
        out.append(hex);
        return out.toString();
    }

}
