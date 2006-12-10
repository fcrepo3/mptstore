package org.nsdl.mptstore.core;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import org.nsdl.mptstore.query.QueryResults;
import org.nsdl.mptstore.query.lang.QueryLanguage;

import org.nsdl.mptstore.rdf.Literal;
import org.nsdl.mptstore.rdf.Node;
import org.nsdl.mptstore.rdf.ObjectNode;
import org.nsdl.mptstore.rdf.PredicateNode;
import org.nsdl.mptstore.rdf.SubjectNode;
import org.nsdl.mptstore.rdf.Triple;
import org.nsdl.mptstore.rdf.URIReference;

import org.nsdl.mptstore.TestConfig;

public abstract class DatabaseAdaptorIntegrationTest {

    private static final String XSD_NS = "http://www.w3.org/2001/XMLSchema#";

    private static URIReference XSD_INT;

    private static URIReference RESOURCE_ONE;
    private static URIReference RESOURCE_TWO;

    private static URIReference TITLE;
    private static URIReference ENGLISH_TITLE;
    private static URIReference SPANISH_TITLE;
    private static URIReference RESOURCE_NUM;
    private static URIReference NEXT_RESOURCE;
    private static URIReference PREV_RESOURCE;

    private static Literal      R1_TITLE;
    private static Literal      R1_ENGLISH_TITLE;
    private static Literal      R1_SPANISH_TITLE;
    private static Literal      R1_RESOURCE_NUM;

    private static Literal      R2_TITLE;
    private static Literal      R2_ENGLISH_TITLE;
    private static Literal      R2_SPANISH_TITLE;
    private static Literal      R2_RESOURCE_NUM;

    private static DataSource POOL;

    private static DatabaseAdaptor ADAPTOR;

    @BeforeClass
    public static void setUpClass() {
        TestConfig.init();
        POOL = TestConfig.getTestDataSource(2);
        try {
            XSD_INT          = new URIReference(XSD_NS + "int");
 
            RESOURCE_ONE     = new URIReference("urn:resource:1");
            RESOURCE_TWO     = new URIReference("urn:resource:2");
 
            TITLE            = new URIReference("urn:pred:title");
            ENGLISH_TITLE    = new URIReference("urn:pred:englishTitle");
            SPANISH_TITLE    = new URIReference("urn:pred:spanishTitle");
            RESOURCE_NUM     = new URIReference("urn:pred:resourceNum");
            NEXT_RESOURCE    = new URIReference("urn:pred:nextResource");
            PREV_RESOURCE    = new URIReference("urn:pred:prevResource");

            R1_TITLE         = new Literal("Resource One");
            R1_ENGLISH_TITLE = new Literal("Resource One", "en");
            R1_SPANISH_TITLE = new Literal("\u00BFHabla espa\u00F1ol?", "es");
            R1_RESOURCE_NUM  = new Literal("1", XSD_INT);

            R2_TITLE         = new Literal("Resource Two");
            R2_ENGLISH_TITLE = new Literal("Resource Two", "en");
            R2_SPANISH_TITLE = new Literal("Recurso Dos", "es");
            R2_RESOURCE_NUM  = new Literal("2", XSD_INT);

        } catch (Exception e) {
            throw new RuntimeException("Error initting constants", e);
        }
    }

    @Before
    public void setUp() throws Exception {
        ADAPTOR = getAdaptor(POOL, "tMap", "t");
        clearTriples(false);
    }

    private static void clearTriples(boolean andMapTable) throws Exception {
        Connection conn = POOL.getConnection();
        try {
            ADAPTOR.deleteAllTriples(conn);
            if (andMapTable) {
            try {
                executeUpdates(conn, TestConfig.getDDLGenerator().getDropMapTableDDL("tMap"));
            } catch (Throwable th) {
            }
            }
        } finally {
            conn.close();
        }
    }

    @After
    public void tearDown() throws Exception {
        clearTriples(false); 
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        clearTriples(true); 
    }

    public abstract DatabaseAdaptor getAdaptor(DataSource pool,
                                               String mapTable,
                                               String soTablePrefix) 
            throws Exception;

    /**
     * Test that after adding, then deleting our test set
     * of triples, querying for all triples returns an
     * empty set.
     */
    @Test
    public void testDeleteTriples() throws Exception {
        Set<Triple> input = getTestTriples();
        add(input);
        delete(input);
        Set<Triple> output = spo("* * *");
        assertEquals(0, output.size());
    }

    /**
     * Test that after adding our test set of triples,
     * querying for all triples returns the same set.
     */
    @Test
    public void testAddTriples() throws Exception {
        Set<Triple> input = getTestTriples();
        add(input);
        Set<Triple> output = spo("* * *");
        assertEquals(input, output);
    }

    @Test
    public void testQuerySPOAll() throws Exception {
        // this case is covered by testAddTriples
    }

    @Test
    public void testQuerySPOWithFixedS() throws Exception {
        Set<Triple> input = getTestTriples();
        add(input);

        Set<Triple> output = spo("<urn:resource:1> * *");
        assertEquals(5, output.size());

        output = spo("<urn:resource:2> * *");
        assertEquals(5, output.size());

        output = spo("<urn:resource:bogus> * *");
        assertEquals(0, output.size());
    }

    @Test
    public void testQuerySPOWithFixedSP() throws Exception {
        Set<Triple> input = getTestTriples();
        add(input);

        Set<Triple> output = spo("<urn:resource:1> <urn:pred:title> *");
        assertEquals(1, output.size());

        output = spo("<urn:resource:2> <urn:pred:title> *");
        assertEquals(1, output.size());

        output = spo("<urn:resource:2> <urn:pred:bogus> *");
        assertEquals(0, output.size());

        output = spo("<urn:resource:bogus> <urn:pred:title> *");
        assertEquals(0, output.size());

        output = spo("<urn:resource:bogus> <urn:pred:bogus> *");
        assertEquals(0, output.size());
    }

    @Test
    public void testQuerySPOWithFixedSPO() throws Exception {
        Set<Triple> input = getTestTriples();
        add(input);

        Set<Triple> output = spo("<urn:resource:1> <urn:pred:title> \"Resource One\"");
        assertEquals(1, output.size());

        output = spo("<urn:resource:1> <urn:pred:title> \"Bogus\"");
        assertEquals(0, output.size());

        output = spo("<urn:resource:1> <urn:pred:bogus> \"Resource One\"");
        assertEquals(0, output.size());

        output = spo("<urn:resource:1> <urn:pred:bogus> \"Bogus\"");
        assertEquals(0, output.size());

        output = spo("<urn:resource:bogus> <urn:pred:title> \"Resource One\"");
        assertEquals(0, output.size());

        output = spo("<urn:resource:bogus> <urn:pred:title> \"Bogus\"");
        assertEquals(0, output.size());

        output = spo("<urn:resource:bogus> <urn:pred:bogus> \"Resource One\"");
        assertEquals(0, output.size());

        output = spo("<urn:resource:bogus> <urn:pred:bogus> \"Bogus\"");
        assertEquals(0, output.size());
    }

    @Test
    public void testQuerySPOWithFixedP() throws Exception {
        Set<Triple> input = getTestTriples();
        add(input);

        Set<Triple> output = spo("* <urn:pred:title> *");
        assertEquals(2, output.size());

        output = spo("* <urn:pred:bogus> *");
        assertEquals(0, output.size());
    }

    @Test
    public void testQuerySPOWithFixedPO() throws Exception {
        Set<Triple> input = getTestTriples();
        add(input);

        Set<Triple> output = spo("* <urn:pred:title> \"Resource One\"");
        assertEquals(1, output.size());

        output = spo("* <urn:pred:bogus> \"Resource One\"");
        assertEquals(0, output.size());

        output = spo("* <urn:pred:title> \"Bogus\"");
        assertEquals(0, output.size());

        output = spo("* <urn:pred:bogus> \"Bogus\"");
        assertEquals(0, output.size());
    }

    @Test
    public void testQuerySPOWithFixedO() throws Exception {
        Set<Triple> input = getTestTriples();
        add(input);

        Set<Triple> output = spo("* * " + R1_TITLE.toString());
        assertEquals(1, output.size());

        output = spo("* * " + R1_ENGLISH_TITLE.toString());
        assertEquals(1, output.size());

        output = spo("* * " + R1_SPANISH_TITLE.toString());
        assertEquals(1, output.size());

        output = spo("* * " + R1_RESOURCE_NUM.toString());
        assertEquals(1, output.size());

        output = spo("* * \"Bogus\"");
        assertEquals(0, output.size());

    }

    /**
     * Get our test set of ten triples.
     */
    private static Set<Triple> getTestTriples() throws Exception {

        Set<Triple> triples = new HashSet<Triple>();

        triples.add(new Triple(RESOURCE_ONE, TITLE, R1_TITLE));
        triples.add(new Triple(RESOURCE_ONE, ENGLISH_TITLE, R1_ENGLISH_TITLE));
        triples.add(new Triple(RESOURCE_ONE, SPANISH_TITLE, R1_SPANISH_TITLE));
        triples.add(new Triple(RESOURCE_ONE, RESOURCE_NUM, R1_RESOURCE_NUM));
        triples.add(new Triple(RESOURCE_ONE, NEXT_RESOURCE, RESOURCE_TWO));

        triples.add(new Triple(RESOURCE_TWO, TITLE, R2_TITLE));
        triples.add(new Triple(RESOURCE_TWO, ENGLISH_TITLE, R2_ENGLISH_TITLE));
        triples.add(new Triple(RESOURCE_TWO, SPANISH_TITLE, R2_SPANISH_TITLE));
        triples.add(new Triple(RESOURCE_TWO, RESOURCE_NUM, R2_RESOURCE_NUM));
        triples.add(new Triple(RESOURCE_TWO, PREV_RESOURCE, RESOURCE_ONE));

        return triples;
    }

    private Set<Triple> spo(String query) throws Exception {

        Set<Triple> triples = new HashSet<Triple>();

        Connection conn = POOL.getConnection();
        conn.setAutoCommit(false);
        QueryResults results = ADAPTOR.query(conn,
                                              QueryLanguage.SPO,
                                              TestConfig.getFetchSize(),
                                              true,
                                              query);
        while (results.hasNext()) {
            List<Node> row = results.next();
            if (row.size() == 3) {
                triples.add(new Triple((SubjectNode) row.get(0),
                                       (PredicateNode) row.get(1),
                                       (ObjectNode) row.get(2)));
            } else {
                throw new RuntimeException("Error, query columns for row != 3");
            }
        }
        results.close();

        return triples;
    }

    private void add(Set<Triple> triples) throws Exception {
        Connection conn = POOL.getConnection();
        try {
            conn.setAutoCommit(false);
            ADAPTOR.addTriples(conn, triples.iterator());
        } finally {
            conn.setAutoCommit(true);
            conn.close();
        }
    }

    private void delete(Set<Triple> triples) throws Exception {
        Connection conn = POOL.getConnection();
        try {
            conn.setAutoCommit(false);
            ADAPTOR.deleteTriples(conn, triples.iterator());
        } finally {
            conn.setAutoCommit(true);
            conn.close();
        }
    }

    private static void executeUpdates(Connection conn, 
                                List<String> sql) throws SQLException {

        Iterator<String> iter = sql.iterator();
        Statement st = conn.createStatement();
        try {
            while (iter.hasNext()) {
                st.executeUpdate(iter.next()); 
            }
        } finally {
            st.close();
        }
    }

}
