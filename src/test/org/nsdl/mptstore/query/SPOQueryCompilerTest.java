package org.nsdl.mptstore.query;

import junit.framework.TestCase;
import junit.swingui.TestRunner;

public class SPOQueryCompilerTest extends TestCase {

    public SPOQueryCompilerTest(String name) { super (name); }

    public void setUp() {
    }
            
    public void tearDown() {
    }

    //
    // Parse Tests
    //

    private boolean isValidQuery(String query) {
        try {
            SPOQueryCompiler.parse(query);
            return true;
        } catch (QuerySyntaxException e) {
            return false;
        }
    }

    public void testParseValidQueries() {
        assertTrue(isValidQuery("* * *"));
        assertTrue(isValidQuery("* * \"test\""));
        assertTrue(isValidQuery("* * \"Here's a quote: \\\"\""));
        assertTrue(isValidQuery("<urn:a> <urn:b> <urn:c>"));
        assertTrue(isValidQuery("<urn:a> <urn:b> \"test\""));
    }

    public void testParseInvalidQueries() {
        assertFalse(isValidQuery(""));
        assertFalse(isValidQuery("* *"));
        assertFalse(isValidQuery("* * * *"));
        assertFalse(isValidQuery("a * *"));
        assertFalse(isValidQuery("* <> *"));
        assertFalse(isValidQuery("* <urn:test> \""));
        assertFalse(isValidQuery("* <urn:test> \'"));
        assertFalse(isValidQuery("<urn:test> <urn:test> 2"));
    }

    public static void main(String[] args) {
        TestRunner.run(SPOQueryCompilerTest.class);
    }   

}
