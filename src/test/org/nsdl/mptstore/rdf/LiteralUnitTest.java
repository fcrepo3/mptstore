package org.nsdl.mptstore.rdf;

import java.text.ParseException;

import junit.framework.TestCase;
import junit.swingui.TestRunner;

public class LiteralUnitTest extends TestCase {

    public LiteralUnitTest(String name) { super (name); }

    public void setUp() {
    }
            
    public void tearDown() {
    }

    public void testPlainLiteralNoLanguage() throws Exception {

        Literal l = new Literal("1");

        // make sure getters return what's exepcted
        assertNull(l.getLanguage());
        assertNull(l.getDatatype());
        assertEquals("1", l.getValue());
        assertEquals("\"1\"", l.toString());

        // object equality and hashCode equality
        Literal same = new Literal("1");
        assertEquals(l, same);
        assertEquals(same, l);
        assertEquals(l.hashCode(), same.hashCode());

        // inequality with different lex
        Literal l2 = new Literal("2");
        assertFalse(l.equals(l2));
        assertFalse(l2.equals(l));

        // inequality with same lex, specified language
        Literal l3 = new Literal("1", "en");
        assertFalse(l3.equals(l));
        assertFalse(l.equals(l3));

        // inequality with same lex, specified datatype
        URIReference type = new URIReference("urn:someDatatype");
        Literal l4 = new Literal("1", type);
        assertFalse(l4.equals(l));
        assertFalse(l.equals(l4));

    }

    public void testPlainLiteralWithLanguage() throws Exception {

        Literal l = new Literal("1", "EN");

        // make sure language normalization worked
        assertEquals("en", l.getLanguage());

        // make sure other getters return what's expected.
        assertNull(l.getDatatype());
        assertEquals("1", l.getValue());
        assertEquals("\"1\"@en", l.toString());

        // object equality and hashCode equality
        Literal same = new Literal("1", "en");
        assertEquals(l, same);
        assertEquals(same, l);
        assertEquals(l.hashCode(), same.hashCode());

        // inequality with different lex
        Literal l2 = new Literal("2", "en");
        assertFalse(l.equals(l2));
        assertFalse(l2.equals(l));

        // inequality with same lex, different language
        Literal l3 = new Literal("1", "es");
        assertFalse(l3.equals(l));
        assertFalse(l.equals(l3));

        // inequality with same lex, no language, and specified datatype
        URIReference type = new URIReference("urn:someDatatype");
        Literal l4 = new Literal("1", type);
        assertFalse(l4.equals(l));
        assertFalse(l.equals(l4));

    }

    public void testTypedLiteral() throws Exception {

        URIReference type = new URIReference("urn:someDatatype");
        Literal l = new Literal("1", type);

        // make sure getters return what's exepcted
        assertNull(l.getLanguage());
        assertEquals(type, l.getDatatype());
        assertEquals("1", l.getValue());
        assertEquals("\"1\"^^" + type.toString(), l.toString());

        // object equality and hashCode equality
        Literal same = new Literal("1", type);
        assertEquals(l, same);
        assertEquals(same, l);
        assertEquals(l.hashCode(), same.hashCode());

        // inequality with different lex
        Literal l2 = new Literal("2", type);
        assertFalse(l.equals(l2));
        assertFalse(l2.equals(l));

        // inequality with same lex, different datatype
        URIReference otherType = new URIReference("urn:someOtherDatatype");
        Literal l3 = new Literal("1", otherType);
        assertFalse(l3.equals(l));
        assertFalse(l.equals(l3));

        // inequality with same lex, no datatype, and specified language
        Literal l4 = new Literal("1", "en");
        assertFalse(l4.equals(l));
        assertFalse(l.equals(l4));

    }

    public static void main(String[] args) {
        TestRunner.run(LiteralUnitTest.class);
    }   

}
