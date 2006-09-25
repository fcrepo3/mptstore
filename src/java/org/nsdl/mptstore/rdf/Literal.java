package org.nsdl.mptstore.rdf;

import java.net.URI;
import java.net.URISyntaxException;

import java.text.ParseException;

/**
 * An RDF Literal.
 * 
 * @see <a href="http://www.w3.org/TR/rdf-concepts/#section-Graph-Literal">
 *      RDF Concepts and Abstract Syntax, Section 6.5</a>
 */
public class Literal implements ObjectNode {

    private String _value;
    private String _language;
    private URIReference _datatype;

    /**
     * Construct a plain literal without a language tag.
     */
    public Literal(String value) {
        _value = value;
    }

    /**
     * Construct a plain literal with a language tag.
     * <p>
     * As per RFC3066, the language tag must be of the
     * following form:
     * <pre>
     *   Language-Tag = Primary-subtag *( "-" Subtag )
     *   Primary-subtag = 1*8ALPHA
     *   Subtag = 1*8(ALPHA / DIGIT)
     * </pre>
     * </p>
     * <p>
     * As per "RDF Concepts and Abstract Syntax", the
     * language tag will be normalized to lowercase.
     * </p>
     *
     * @throws ParseException if the language is syntactically
     *         invalid according to RFC3066.
     */
    public Literal(String value, String language) throws ParseException {
        _value = value;
        if (language != null) {
            if (language.length() == 0) {
                throw new ParseException("Expected [A-Za-z]", 0);
            }
            if (language.indexOf(" ") != -1) {
                throw new ParseException("Space character not allowed", 
                        language.indexOf(" "));
            }

            String[] parts = language.split("-");
            for (int i = 0; i < parts.length; i++) {
                if (parts[i].length() < 1 || parts[i].length() > 8) {
                    throw new ParseException("Subtags must be between 1 and 8 chars", 0);
                }
                for (int j = 0; j < parts[i].length(); j++) {
                    char c = parts[i].charAt(j);
                    if (   (c >= 'a' && c <= 'z') 
                        || (c >= 'A' && c <= 'Z')) {
                    } else if (    (i > 0) 
                                && (c >= '0' && c <= '9') ) {
                    } else {
                        throw new ParseException("Subtag cannot contain character '" 
                                + c + "'", 0);
                    }
                }
            }

            _language = language.toLowerCase();
        }
    }

    public Literal(String value, URIReference datatype) {
        _value = value;
        _datatype = datatype;
    }

    public String getLanguage() {
        return _language;
    }

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
