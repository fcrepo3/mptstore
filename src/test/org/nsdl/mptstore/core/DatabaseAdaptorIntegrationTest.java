package org.nsdl.mptstore.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

import javax.sql.DataSource;

import junit.framework.TestCase;
import junit.swingui.TestRunner;

import org.nsdl.mptstore.rdf.Literal;
import org.nsdl.mptstore.rdf.Triple;
import org.nsdl.mptstore.rdf.URIReference;

import org.nsdl.mptstore.TestConfig;

public abstract class DatabaseAdaptorIntegrationTest extends TestCase {

    private DatabaseAdaptor _adaptor;
    private Connection _conn;

    protected DatabaseAdaptorIntegrationTest(String name) {
        super(name); 
    }

    public void setUp() throws Exception {
        DataSource pool = TestConfig.getTestDataSource(2);
        _adaptor = initAdaptor(pool, "tMap", "t");
        _conn = pool.getConnection();
        _adaptor.deleteAllTriples(_conn);
    }

    public void tearDown() throws Exception {
        
        if (_conn != null) {
            _adaptor.deleteAllTriples(_conn);
            executeUpdates(TestConfig.getDDLGenerator().getDropMapTableDDL("tMap"));
            _conn.close();
        }
    }

    public abstract DatabaseAdaptor initAdaptor(DataSource pool,
                                                String mapTable,
                                                String soTablePrefix) 
            throws Exception;

    public void testAddTriples() throws Exception {
        addTriples();
    }

    private void addTriples() throws Exception {

        _conn.setAutoCommit(false);

        try {

            List<Triple> triples = new ArrayList<Triple>();
            triples.add(new Triple(new URIReference("urn:resource:1"),
                                   new URIReference("urn:prop:1"),
                                   new URIReference("urn:resource:2")));
            triples.add(new Triple(new URIReference("urn:resource:1"),
                                   new URIReference("urn:prop:2"),
                                   new URIReference("urn:resource:2")));
            triples.add(new Triple(new URIReference("urn:resource:1"),
                                   new URIReference("urn:prop:2"),
                                   new URIReference("urn:resource:3")));
            triples.add(new Triple(new URIReference("urn:resource:2"),
                                   new URIReference("urn:prop:1"),
                                   new URIReference("urn:resource:2")));
            triples.add(new Triple(new URIReference("urn:resource:2"),
                                   new URIReference("urn:prop:2"),
                                   new URIReference("urn:resource:2")));
            triples.add(new Triple(new URIReference("urn:resource:2"),
                                   new URIReference("urn:prop:2"),
                                   new URIReference("urn:resource:3")));
            _adaptor.addTriples(_conn, triples.iterator());

            _conn.commit();

        } finally {
            _conn.setAutoCommit(true);
        }
    }

    private void executeUpdates(List<String> sql) throws SQLException {

        Iterator<String> iter = sql.iterator();
        Statement st = _conn.createStatement();
        try {
            while (iter.hasNext()) {
                st.executeUpdate(iter.next()); 
            }
        } finally {
            st.close();
        }
    }

}
