package org.nsdl.mptstore.rdf;

import java.text.ParseException;

import org.nsdl.mptstore.util.NTriplesUtil;

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

	public static final int LITERAL_MAXLEN = 255;
	
    /**
     * The lexical value of this literal.
     */
    private String _value;

    /**
     * The normalized language tag of this literal, if any.
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
    public Literal(final String value) {
        _value = value;
    }

    /**
     * Construct a plain literal with a language tag.
     *
     * As per "RDF Concepts and Abstract Syntax", it will
     * be normalized to lowercase.
     *
     * @param value The lexical value.
     * @param language The language tag.
     * @throws ParseException if the language tag is syntactically invalid.
     * @see NTriplesUtil#validateLanguage(String)
     */
    public Literal(final String value,
                   final String language)
            throws ParseException {
        _value = value;
        if (language != null) {
            NTriplesUtil.validateLanguage(language);
            _language = language.toLowerCase();
        }
    }

    /**
     * Construct a typed literal.
     *
     * @param value The lexical value.
     * @param datatype The datatype.
     */
    public Literal(final String value,
                   final URIReference datatype) {
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

    /** {@inheritDoc} */
    public String getValue() {
        return _value;
    }

    /** {@inheritDoc} */
    public String toString() {
        StringBuffer out = new StringBuffer();
        out.append('"');
        
        // 5 is the length of "" and ...
        int suffixLenght = 5;
        if(_language != null){
        	// it might be 2 or 3 letter code
        	suffixLenght += 1 + _language.length();
        }
        if(_datatype != null){
        	suffixLenght += 2 + _datatype.toString().length();
        }
        
        int stringMaxLenght = 0;
        if(LITERAL_MAXLEN > 0){
        	stringMaxLenght = LITERAL_MAXLEN-suffixLenght;
        }

        out.append(NTriplesUtil.escapeAndTruncateLiteralValue(_value, stringMaxLenght));      
        
        out.append('"');
        if (_language != null) {
            out.append("@" + _language);
        } else if (_datatype != null) {
            out.append("^^" + _datatype.toString());
        }

        return out.toString();
    }

    /** {@inheritDoc} */
    public boolean equals(final Object obj) {
        if (obj != null && obj instanceof Literal) {
            Literal lit = (Literal) obj;
            if (_language != null) {
                return _language.equals(lit.getLanguage())
                    && _value.equals(lit.getValue());
            } else if (_datatype != null) {
                return _datatype.equals(lit.getDatatype())
                    && _value.equals(lit.getValue());
            } else {
                return lit.getLanguage() == null
                    && lit.getDatatype() == null
                    && _value.equals(lit.getValue());
            }
        } else {
            return false;
        }
    }

    /** {@inheritDoc} */
    public int hashCode() {
        return _value.hashCode();
    }

}
