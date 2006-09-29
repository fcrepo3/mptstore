package org.nsdl.mptstore.query.lang.spo;

import junit.framework.TestCase;
import junit.swingui.TestRunner;

import org.nsdl.mptstore.core.TableManager;
import org.nsdl.mptstore.query.QueryException;
import org.nsdl.mptstore.rdf.PredicateNode;

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

    public String getOrMapTableFor(PredicateNode predicate) {
        return null;
    }

    public String getTableFor(PredicateNode predicate) {
        return null;
    }

    public PredicateNode getPredicateFor(String table) {
        return null;
    }

    public java.util.Set<String> getTables() {
        return new java.util.HashSet<String>();
    }

    public java.util.Set<PredicateNode> getPredicates() {
        return new java.util.HashSet<PredicateNode>();
    }

    public int dropEmptyPredicateTables() {
        return 0;
    }

    public int dropAllPredicateTables() {
        return 0;
    }
}

}
