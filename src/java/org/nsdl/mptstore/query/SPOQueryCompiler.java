package org.nsdl.mptstore.query;

import java.util.ArrayList;
import java.util.List;

import org.nsdl.mptstore.core.TableManager;

/**
 * Compiler for SPO queries.
 *
 * SPO is a simple query language that returns triples (subject, predicate,
 * object) given a single triple pattern where an asterisk in any
 * position means "any".
 *
 * <p>The grammar for SPO queries is:</p>
 *
 * <pre>
 *   query         := nodeSpec ' ' nodeSpec ' ' nodeSpec
 *   nodeSpec      := '*' | rdfNode
 *   rdfNode       := uriToken | literalToken
 *   uriToken      := '&lt;' absoluteURI '&gt;'
 *   literalToken  := quote escapedString quote [ langQual | typeQual ]
 *   quote         := '"' | '''
 *   langQual      := '@' langCode
 *   typeQual      := '^^' absoluteURI
 *   escapedString := any sequence of characters, with double quotes escaped 
 *                    as \" and backslashes escaped as \\
 * </pre>
 */
public class SPOQueryCompiler implements QueryCompiler {

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
        List<String> parsedQuery = parse(query);
        return new SPOSQLProvider(_tableManager,
                                  _backslashIsEscape,
                                  parsedQuery.get(0),
                                  parsedQuery.get(1),
                                  parsedQuery.get(2));
    }

    /**
     * Parse the given query.
     *
     * @return a list of three normalized strings representing rdf nodes.
     */
    protected static List<String> parse(String query)
            throws QuerySyntaxException {

        query = query.trim();

        List<String> tokens = new ArrayList<String>();

        StringBuffer token = null;
        boolean inQuotes = false;
        boolean inEscapeSequence = false;
        char quoteChar = '"';

        for (int i = 0; i < query.length(); i++) {

            char c = query.charAt(i);

            if (token != null) {
                if (inQuotes) {
                    if (c == quoteChar) {
                        if (!inEscapeSequence) {
                            inQuotes = false;
                        } else {
                            inEscapeSequence = false;
                        }
                        token.append(c);
                    } else {
                        if (!inEscapeSequence) {
                            if (c == '\\') {
                                inEscapeSequence = true;
                            }
                        } else {
                            inEscapeSequence = false;
                        }
                        token.append(c);
                    }
                } else {
                    if (isWhitespace(c)) {
                        // end token
                        String tokenString = token.toString();
                        validate(tokenString);
                        tokens.add(normalize(tokenString));
                        token = null;
                    } else {
                        if (c == '\'' || c == '"') {
                            inQuotes = true;
                            quoteChar = c;
                        }
                        token.append(c);
                    }
                }
            } else {
                if (!isWhitespace(c)) {
                    // start token
                    token = new StringBuffer();
                    token.append(c);
                }
            }

        }

        // end of input...add final token
        if (token != null) {
            String tokenString = token.toString();
            validate(tokenString);
            tokens.add(normalize(tokenString));
        }

        if (tokens.size() != 3) {
            throw new QuerySyntaxException("SPO query must have exactly 3 "
                    + "tokens");
        }

        return tokens;
    }

    private static boolean isWhitespace(char c) {
        return (c == ' ' || c == '\t' || c == '\n' || c == '\r');
    }

    /**
     * Validate the given token.
     *
     * Valid tokens include:
     * <ul>
     *   <li> *</li>
     *   <li> &lt;resourceURI&gt;</li>
     *   <li> &quot;literal&quot;</li>
     *   <li> &apos;literal&apos;</li>
     *   <li> &quot;literal&quot;@langCode</li>
     *   <li> &apos;literal&apos;@langCode</li>
     *   <li> &quot;literal&quot;^^datatypeURI</li>
     *   <li> &apos;literal&apos;^^datatypeURI</li>
     * </ul>
     */
    protected static void validate(String token)
            throws QuerySyntaxException {

        if (token.startsWith("<")) {
            validateURIToken(token);
        } else if (token.startsWith("\"") || token.startsWith("'")) {
            validateLiteralToken(token);
        } else if (!token.equals("*")) {
            throw new QuerySyntaxException("Bad query syntax: '" + token + "'");
        }
    }

    protected static void validateURIToken(String token)
            throws QuerySyntaxException {
        if (!token.endsWith(">")) {
            throw new QuerySyntaxException("URI in query must end "
                    + "with '>' character.");
        } else {
            validateURI(token.substring(1, token.length() - 1));
        }
    }

    protected static void validateURI(String uri) 
            throws QuerySyntaxException {
        try {
            if (!(new java.net.URI(uri)).isAbsolute()) {
                throw new QuerySyntaxException("URI is not absolute: " + uri);
            }
        } catch (java.net.URISyntaxException e) {
            throw new QuerySyntaxException("Malformed URI: " + uri);
        }
    }

    protected static void validateLiteralToken(String token)
            throws QuerySyntaxException {
        String quoteChar = "" + token.charAt(0);
        int i = token.lastIndexOf(quoteChar);
        if (i > 0) {
            String lex = validateEscapedLiteralString(token.substring(1, i));
            validateLiteralQualifier(token.substring(i + 1));
        } else {
            throw new QuerySyntaxException("Malformed literal token: " + token);
        }
    }

    /**
     * Validate the following for the quoted string:
     * - Can't end with un-escaped \ character
     * - Can't have " chars outside of escape sequence
     * ...then return the un-escaped string
     */
    protected static String validateEscapedLiteralString(String s)
            throws QuerySyntaxException {

        if ( (s.indexOf("\\") == -1) 
                && (s.indexOf("\"") == -1) ) {
            return s;
        }

        StringBuffer out = new StringBuffer();
        boolean inEscapeSequence = false;

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (inEscapeSequence) {
                inEscapeSequence = false;
                if (c != '"' && c != '\\') {
                    // retain escape char for all but " and \
                    out.append('\\');
                }
                out.append(c);
            } else {
                if (c == '\\') {
                    inEscapeSequence = true;
                } else if (c == '"') {
                    throw new QuerySyntaxException("Double quote character "
                            + "must be \\ escaped in literal: " + s);
                } else {
                    out.append(c);
                }
            }
        }

        if (inEscapeSequence) {
            throw new QuerySyntaxException("Quoted literal value cannot end "
                    + "with escape character: " + s);
        }
        return out.toString();
    }

    protected static void validateLiteralQualifier(String qualifier)
            throws QuerySyntaxException {
        if (qualifier.startsWith("^^")) {
            validateURI(qualifier.substring(2));
        } else if (qualifier.startsWith("@")) {
            if (qualifier.length() == 1) {
                throw new QuerySyntaxException("Localized literal does not " 
                        + "specify a language code");
            }
        } else if (qualifier.length() > 0) {
            throw new QuerySyntaxException("Bad literal qualifier: " 
                    + qualifier);
        }
    }

    /**
     * Normalize the given token from the query.
     *
     * Literals will be normalized to use double quote characters
     * and unbounds (asterisks) will be represented with 
     * <code>null</code> values.
     */
    protected static String normalize(String token) {
        if (token.equals("*")) {
            return null;
        } else if (token.startsWith("'")) {
            // replace first and last ' with "
            int i = token.lastIndexOf("'");
            StringBuffer buf = new StringBuffer();
            buf.append('"');
            buf.append(token.substring(1, i));
            buf.append('"');
            buf.append(token.substring(i + 1));
            return buf.toString();
        } else {
            return token;
        }
    }

}
