package org.nsdl.mptstore.query;

import junit.framework.TestCase;
import junit.swingui.TestRunner;

import org.nsdl.mptstore.core.TableManager;

public class SPOQueryCompilerTest extends TestCase {

    private SPOQueryCompiler _compiler;

    public SPOQueryCompilerTest(String name) { super (name); }

    public void setUp() {
        _compiler = new SPOQueryCompiler(new FakeTableManager(), true);
    }
            
    public void tearDown() {
    }

    //
    // Parse Tests
    //

    private boolean isValidQuery(String query) {
        try {
            _compiler.compile(query);
            return true;
        } catch (QueryException e) {
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

private class FakeTableManager implements TableManager {

    public String getOrMapTableFor(String predicate) {
        return null;
    }

    public String getTableFor(String predicate) {
        return null;
    }

    public String getPredicateFor(String table) {
        return null;
    }

    public java.util.Set<String> getTables() {
        return new java.util.HashSet();
    }

    public java.util.Set<String> getPredicates() {
        return new java.util.HashSet();
    }

    public int dropEmptyPredicateTables() {
        return 0;
    }

    public int dropAllPredicateTables() {
        return 0;
    }
}

}
