package org.nsdl.mptstore.rdf;

import java.net.URI;
import java.net.URISyntaxException;

import java.text.ParseException;

/**
 * An RDF literal.
 *
 * A literal has a lexical component and optionally has either a language
 * tag or a datatype (indicated by URIReference).
 * 
 * @author cwilper@cs.cornell.edu
 * @see <a href="http://www.w3.org/TR/rdf-concepts/#section-Graph-Literal">
 *      RDF Concepts and Abstract Syntax, Section 6.5</a>
 */
public class Literal implements ObjectNode {

    /**
     * The lexical value of this literal.
     */
    private String _value;

    /**
     * The language tag of this literal, if any.
     */
    private String _language;

    /**
     * The datatype of this literal, if any.
     */
    private URIReference _datatype;

    /**
     * Construct a plain literal without a language tag.
     *
     * @param value The lexical value.
     */
    public Literal(String value) {
        _value = value;
    }

    /**
     * Construct a plain literal with a language tag.
     * <p>
     *   The language tag must be of the following form:
     *   <pre>
     *     Language-Tag = Primary-subtag *( "-" Subtag )
     *     Primary-subtag = 1*8ALPHA
     *     Subtag = 1*8(ALPHA / DIGIT)
     *   </pre>
     * </p>
     * <p>
     *   As per "RDF Concepts and Abstract Syntax", the
     *   language tag will be normalized to lowercase.
     * </p>
     *
     * @param value The lexical value.
     * @param language The language tag.
     * @throws ParseException if the language is syntactically
     *         invalid according to RFC3066.
     */
    public Literal(String value, String language) throws ParseException {
        _value = value;
        if (language != null) {
            if (language.length() == 0) {
                throw new ParseException("Language tag must not be empty", 0);
            }
            if (language.indexOf(" ") != -1) {
                throw new ParseException("Space character not allowed in "
                        + "language tag", language.indexOf(" "));
            }

            String[] parts = language.split("-");
            for (int i = 0; i < parts.length; i++) {
                if (parts[i].length() < 1 || parts[i].length() > 8) {
                    throw new ParseException("Language subtags must be "
                            + "1-8 characters long", 0);
                }
                for (int j = 0; j < parts[i].length(); j++) {
                    char c = parts[i].charAt(j);
                    if (   (c >= 'a' && c <= 'z') 
                        || (c >= 'A' && c <= 'Z')) {
                    } else if (    (i > 0) 
                                && (c >= '0' && c <= '9') ) {
                    } else {
                        throw new ParseException("Language subtag cannot "
                                + "contain character '" + c + "'", 0);
                    }
                }
            }

            _language = language.toLowerCase();
        }
    }

    /**
     * Construct a typed literal.
     *
     * @param value The lexical value.
     * @param datatype The datatype.
     */
    public Literal(String value, URIReference datatype) {
        _value = value;
        _datatype = datatype;
    }

    /**
     * Get the language tag, if any.
     *
     * @return The language tag, or <code>null</code> if none.
     */
    public String getLanguage() {
        return _language;
    }

    /**
     * Get the datatype, if any.
     *
     * @return The datatype, or <code>null</code> if none.
     */
    public URIReference getDatatype() {
        return _datatype;
    }

    // Implements Node.getValue()
    public String getValue() {
        return _value;
    }

    // Implements Node.toString()
    public String toString() {
        StringBuffer out = new StringBuffer();
        out.append('"');
        out.append(NTParser.escape(_value));
        out.append('"');
        if (_language != null) {
            out.append("@" + _language);
        } else if (_datatype != null) {
            out.append("^^" + _datatype.getValue());
        }
        return out.toString();
    }

}
