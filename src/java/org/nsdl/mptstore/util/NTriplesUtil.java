package org.nsdl.mptstore.util;

import java.io.IOException;
import java.io.Reader;
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

    private static final int HIGHEST_ASCII_CHAR = 127;
    private static final int HEX = 16;
    private static final int SHORT_ESCAPE_LENGTH = 5;
    private static final int LONG_ESCAPE_LENGTH = 10;

    private static final int UC_LOW1 = 0x0;
    private static final int UC_HIGH1 = 0x8;

    private static final int UC_LOW2 = 0xB;
    private static final int UC_HIGH2 = 0xC;

    private static final int UC_LOW3 = 0xE;
    private static final int UC_HIGH3 = 0x1F;

    private static final int UC_LOW4 = 0x7F;
    private static final int UC_HIGH4 = 0xFFFF;

    private static final int UC_LOW5 = 0x10000;
    private static final int UC_HIGH5 = 0x10FFFF;

    private static final String EXPECTED_ABS_URI = "Expected absolute URI";
    private static final String EXPECTED_ACE = "Expected '@', '^', or EOF";
    private static final String EXPECTED_C = "Expected '^'";
    private static final String EXPECTED_G = "Expected '>'";
    private static final String EXPECTED_L = "Expected '<'";
    private static final String EXPECTED_LST = "Expected '<', ' ', or TAB";
    private static final String EXPECTED_PST = "Expected '.', ' ', or TAB";
    private static final String EXPECTED_Q = "Expected '\"'";
    private static final String EXPECTED_QL = "Expected '\"' or '<'";
    private static final String EXPECTED_QLST = "Expected '\"', '<', ' ', "
            + "or TAB";
    private static final String EXPECTED_ST = "Expected ' ' or TAB";
    private static final String UNEXPECTED_EOF = "Unexpected EOF";
    private static final String NON_ASCII_CHAR = "Non-ASCII character";
    private static final String UNESCAPED_BACKSLASH = "Unescaped backslash";
    private static final String ILLEGAL_ESCAPE = "Illegal Unicode escape "
            + "sequence";
    private static final String INCOMPLETE_ESCAPE = "Incomplete Unicode "
            + "escape sequence";
    private static final String UNESCAPED_CR = "Unescaped carriage return";
    private static final String UNESCAPED_LF = "Unescaped linefeed";
    private static final String UNESCAPED_TAB = "Unescaped tab";

    private static final int[] SPACE_OR_TAB = new int[] {' ', '\t'};

    private NTriplesUtil() { }

    /**
     * Consume the next URI reference.
     *
     * This will advance the reader through the next '&gt;' character.
     *
     * @param the reader to get characters from.
     * @param the current position in the overall input.
     * @return all characters up to an including the terminal, '&gt;'.
     * @throws IOException if there's an I/O error reading the input.
     * @throws ParseException if the reader is exhausted before the terminal
     *         is encountered.
     */
    private static String consumeURIReference(final Reader reader,
                                              final int pos)
            throws IOException, ParseException {

        int i = 0;
        StringBuffer buf = new StringBuffer();

        int c = reader.read();
        while (c != '>') {
            if (c == -1) {
                throw new ParseException(EXPECTED_G, pos + i);
            }
            buf.append((char) c);
            i++;
            c = reader.read();
        }
        buf.append((char) c);
        return buf.toString();
    }

    /**
     * Tell whether the given character matches one in the given array.
     *
     * @param chars the array.
     * @parm c the character.
     * @return true if c exists in chars.
     */
    private static boolean isOneOf(final int[] chars,
                                   final int c) {
        for (int i = 0; i < chars.length; i++) {
            if (c == chars[i]) {
                return true;
            }
        }
        return false;
    }

    /**
     * Move the reader to the first non-whitespace character
     * and return the number of whitespaces encountered.
     *
     * @param reader the reader.
     * @param pos the current position in the reader.
     * @return the number of whitespaces encountered.
     * @throw IOException if there is an error reading.
     * @throw ParseException if the first character is not a whitespace or
     *        EOF is reached before a non-whitespace char.
     */
    private static int consumeWhitespace(final Reader reader,
                                         final int pos)
            throws IOException, ParseException {

        int i = 0;

        int c = reader.read();
        if (!isOneOf(SPACE_OR_TAB, c)) {
            throw new ParseException(EXPECTED_ST, pos + i);
        }
        while (isOneOf(SPACE_OR_TAB, c)) {
            if (c == -1) {
                throw new ParseException(UNEXPECTED_EOF, pos + i - 1);
            }
            i++;
            reader.mark(Integer.MAX_VALUE);
            c = reader.read();
        }
        reader.reset();
        return i;
    }

    /**
     * Extract the object part of a triple string.
     *
     * @param ntTriple the full triple string.
     * @param i the position of the first object character.
     * @throws ParseException if the input does not appear to be a valid
     *         triple string.
     */
    private static String getObjectString(final String ntTriple,
                                          final int i)
            throws ParseException {

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

        return ntTriple.substring(i, j + 1);
    }

    /**
     * Parse an RDF triple in N-Triples format.
     *
     * @param ntTriple the input string.
     * @return the parsed triple.
     * @throws ParseException if the input syntax is incorrect
     * @see <a href="http://www.w3.org/TR/rdf-testcases/#triple">
     *        N-Triples triple syntax</a>
     */
    public static Triple parseTriple(final String ntTriple)
            throws ParseException {

        StringReader reader = new StringReader(ntTriple);

        try {
            int i = 0;

            // subject
            String sString = consumeURIReference(reader, i);
            SubjectNode subject = parseSubject(sString);
            i += sString.length();

            // whitespace+
            i += consumeWhitespace(reader, i);

            // predicate
            String pString = consumeURIReference(reader, i);
            PredicateNode predicate;
            try {
                predicate = parsePredicate(pString);
            } catch (ParseException e) {
                throw new ParseException(e.getMessage(),
                        e.getErrorOffset() + i);
            }
            i += pString.length();

            // whitespace+
            i += consumeWhitespace(reader, i);

            // object
            String oString = getObjectString(ntTriple, i);
            ObjectNode object;
            try {
                object = parseObject(oString);
            } catch (ParseException e) {
                throw new ParseException(e.getMessage(),
                        e.getErrorOffset() + i);
            }

            // triple
            return new Triple(subject, predicate, object);

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
    public static SubjectNode parseSubject(final String ntSubject)
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
    public static PredicateNode parsePredicate(final String ntPredicate)
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
    public static ObjectNode parseObject(final String ntObject)
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
    public static Node parseNode(final String ntNode)
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
     * Advance the reader until a double quote character is encountered
     * and return the characters seen.
     *
     * @param reader the reader.
     * @param pos the current position in the overall string.
     * @throws IOException if there is an error reading.
     * @throws ParseException if EOF is encountered before the terminal
     *         character or an unescaped linefeed, carriage return,
     *         or tab character is encountered.
     */
    private static String consumeLiteralValue(final Reader reader,
                                              final int pos)
            throws IOException, ParseException {

        StringBuffer escaped = new StringBuffer();
        int i = 0;
        int c = reader.read();

        while (c != '"') {

            if (c == -1) {
                throw new ParseException(EXPECTED_Q, pos + i);
            }
            escaped.append((char) c);

            if (c == '\\') {
                c = reader.read();
                i++;
                if (c == -1) {
                    throw new ParseException(EXPECTED_Q, pos + i);
                }
                escaped.append((char) c);
            } else if (c == '\r') {
                throw new ParseException(UNESCAPED_LF, pos + i);
            } else if (c == '\n') {
                throw new ParseException(UNESCAPED_CR, pos + i);
            } else if (c == '\t') {
                throw new ParseException(UNESCAPED_TAB, pos + i);
            }

            c = reader.read();
            i++;
        }

        return escaped.toString();
    }

    /**
     * Parse an RDF literal in N-Triples format.
     *
     * @param s the input string.
     * @return the parsed literal.
     * @throws ParseException if the input syntax is incorrect
     */
    public static Literal parseLiteral(final String s)
            throws ParseException {

        StringReader reader = new StringReader(s);

        try {

            int first = reader.read();

            if (first != '"') {
                throw new ParseException(EXPECTED_Q, 0);
            }

            int i = 1;

            String escaped = consumeLiteralValue(reader, i);
            String value;
            try {
                value = unescapeLiteralValue(escaped);
            } catch (ParseException e) {
                throw new ParseException(e.getMessage(),
                                         e.getErrorOffset() + i);
            }
            i += escaped.length();

            // c == '"', read next char
            int c = reader.read();
            i++;

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
    public static URIReference parseURIReference(final String s)
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
     * Verify all characters of the given string are 7-bit ASCII.
     *
     * @param s the string.
     * @throws ParseException if a non-ASCII character exists in the string.
     */
    private static void verifyAscii(final String s) throws ParseException {
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c > HIGHEST_ASCII_CHAR) {
                throw new ParseException(NON_ASCII_CHAR, i);
            }
        }
    }

    /**
     * Return the next unescaped value from the escaped string.
     *
     * @param s the whole string.
     * @param backslashPos the position of the current escape character.
     * @param i the current position in the string.
     * @param c the current character.
     * @param buf the buffer to append to.
     * @throws ParseException if an incomplete or illegal escape sequence
     *         is encountered.
     */
    private static int appendUnescaped(final String s,
                                       final int backslashPos,
                                       final int i,
                                       final char c,
                                       final StringBuffer buf)
            throws ParseException {


        if (c == 'u') {
            if (backslashPos + SHORT_ESCAPE_LENGTH >= s.length()) {
                throw new ParseException(INCOMPLETE_ESCAPE, i);
            }
            String xx = s.substring(backslashPos + 2,
                    backslashPos + SHORT_ESCAPE_LENGTH + 1);
            try {
                int nextChar = (char) Integer.parseInt(xx, HEX);
                buf.append((char) nextChar);
                return backslashPos + SHORT_ESCAPE_LENGTH + 1;
            } catch (NumberFormatException e) {
                throw new ParseException(ILLEGAL_ESCAPE, i);
            }
        } else if (c == 'U') {
            if (backslashPos + LONG_ESCAPE_LENGTH - 1 >= s.length()) {
                throw new ParseException(INCOMPLETE_ESCAPE, i);
            }
            String xx = s.substring(backslashPos + 2,
                    backslashPos + LONG_ESCAPE_LENGTH);
            try {
                int nextChar = (char) Integer.parseInt(xx, HEX);
                buf.append((char) nextChar);
                return backslashPos + LONG_ESCAPE_LENGTH;
            } catch (NumberFormatException e) {
                throw new ParseException(ILLEGAL_ESCAPE, i);
            }
        } else {
            buf.append(getUnescaped(c, i));
            return backslashPos + 2;
        }

    }

    /**
     * Get the unescaped value for the given character.
     *
     * The character was encountered in the string after an escape
     * character.
     *
     * @param c the character.
     * @param i the current position in the overall string.
     * @throws ParseException if the given character does not
     *         represent a single character found in an escape sequence.
     */
    private static char getUnescaped(final char c,
                                     final int i) throws ParseException {
        if (c == 't') {
            return '\t';
        } else if (c == 'r') {
            return '\r';
        } else if (c == 'n') {
            return '\n';
        } else if (c == '"' || c == '\\') {
            return c;
        } else {
            throw new ParseException(UNESCAPED_BACKSLASH, i);
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
    public static String unescapeLiteralValue(final String s)
            throws ParseException {

        verifyAscii(s);

        int backslashPos = s.indexOf('\\');

        // return early if no escape
        if (backslashPos == -1) {
            return s;
        }

        int i = 0;
        StringBuffer buf = new StringBuffer(s.length());

        // unescape all
        while (backslashPos != -1) {

            buf.append(s.substring(i, backslashPos));

            if (backslashPos + 1 >= s.length()) {
                throw new ParseException(UNESCAPED_BACKSLASH, i);
            }

            char c = s.charAt(backslashPos + 1);
            i = appendUnescaped(s, backslashPos, i, c, buf);

            backslashPos = s.indexOf('\\', i);
        }
        buf.append(s.substring(i));

        return buf.toString();
    }

    /**
     * Escape a string to N-Triples literal format.
     *
     * <ul>
     *   <li> Unicode escaping (&#x5C;uxxxx or &#x5C;Uxxxxxxxx, a
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
    public static String escapeLiteralValue(final String s) {

        int len = s.length();
        StringBuffer out = new StringBuffer(len * 2);

        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            int cNum = (int) c;
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
            } else if (isLowUnicode(cNum)) {
                out.append("\\u");
                out.append(hexString(cNum, SHORT_ESCAPE_LENGTH - 1));
            } else if (isHighUnicode(cNum)) {
                out.append("\\U");
                out.append(hexString(cNum, LONG_ESCAPE_LENGTH - 2));
            } else {
                out.append(c);
            }
        }

        return out.toString();
    }

    /**
     * Tell whether the given character is in the "low unicode"
     * (two-byte) range.
     *
     * @param cNum the character.
     * @return true if it's a low unicode character.
     */
    private static boolean isLowUnicode(final int cNum) {
        return (cNum >= UC_LOW1 && cNum <= UC_HIGH1)
                || (cNum == UC_LOW2 || cNum == UC_HIGH2)
                || (cNum >= UC_LOW3 && cNum <= UC_HIGH3)
                || (cNum >= UC_LOW4 && cNum <= UC_HIGH4);
    }

    /**
     * Tell whether the given character is in the "high unicode"
     * (four-byte) range.
     *
     * @param cNum the character.
     * @return true if it's a low unicode character.
     */
    private static boolean isHighUnicode(final int cNum) {
        return cNum >= UC_LOW5 && cNum <= UC_HIGH5;
    }

    /**
     * Get an uppercase hex string of the specified length,
     * representing the given number.
     *
     * @param num The number to represent.
     * @param len The desired length of the output.
     * @return The uppercase hex string.
     */
    private static String hexString(final int num, final int len) {
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
