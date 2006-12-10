package org.nsdl.mptstore.query.lang.spo;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.nsdl.mptstore.TestConfig;
import org.nsdl.mptstore.core.TableManager;
import org.nsdl.mptstore.query.QueryException;
import org.nsdl.mptstore.rdf.PredicateNode;

public class SPOQueryCompilerUnitTest {

    private SPOQueryCompiler _compiler;

    @BeforeClass
    public static void setUpClass() {
        TestConfig.init();
    }

    @Before
    public void setUp() {
        _compiler = new SPOQueryCompiler(new FakeTableManager(), true);
    }
            
    @Test
    public void testParseValidSPOQueries() {
        assertTrue(isValidQuery("* * *"));
        assertTrue(isValidQuery("* * \"test\""));
        assertTrue(isValidQuery("* * \"Here's a quote: \\\"\""));
        assertTrue(isValidQuery("<urn:a> <urn:b> <urn:c>"));
        assertTrue(isValidQuery("<urn:a> <urn:b> \"test\""));
        assertTrue(isValidQuery("<urn:a> <urn:b> \"test\"@en-US"));
        assertTrue(isValidQuery("<urn:a> <urn:b> \"1\"^^<urn:someDatatype>"));
    }

    @Test
    public void testParseInvalidSPOQueries() {
        assertFalse(isValidQuery(""));
        assertFalse(isValidQuery("* *"));
        assertFalse(isValidQuery("* * * *"));
        assertFalse(isValidQuery("a * *"));
        assertFalse(isValidQuery("* <> *"));
        assertFalse(isValidQuery("* <urn:test> \""));
        assertFalse(isValidQuery("* <urn:test> \'"));
        assertFalse(isValidQuery("<urn:test> <urn:test> 2"));
        assertFalse(isValidQuery("<urn:a> <urn:b> \"test\"@en-"));
        assertFalse(isValidQuery("<urn:a> <urn:b> \"1\"^^<urn:some badDatatype>"));
    }

    private boolean isValidQuery(String query) {
        try {
            _compiler.compile(query);
            return true;
        } catch (QueryException e) {
            return false;
        }
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
