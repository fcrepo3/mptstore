package org.nsdl.mptstore.perftest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import java.sql.Connection;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.nsdl.mptstore.rdf.Triple;
import org.nsdl.mptstore.core.DatabaseAdaptor;
import org.nsdl.mptstore.TestConfig;

public class LoadPerformanceTest {

    /** Logger for this class. */
    private static Logger LOG = Logger.getLogger(LoadPerformanceTest.class);

    private DatabaseAdaptor _adaptor;
    private DataSource _dataSource;

    @BeforeClass
    public static void setUpClass() {
        TestConfig.init();
    }

    @Before
    public void setUp() throws Exception {
        _dataSource = TestConfig.getTestDataSource(2);
        _adaptor = TestConfig.getTestDatabaseAdaptor(_dataSource);
    }

    @Test
    public void testLoadPerformance() throws Exception {
        LOG.info("Starting load test");
        TestTripleFactory factory = TestConfig.getTestTripleFactory();
        int numSubjects = TestConfig.getNumTestSubjects();
        int subjectsPerTransaction = TestConfig.getNumTestSubjectsPerTransaction();
        Connection conn = _dataSource.getConnection();
        int subjectsInTransaction = 0;
        Set<Triple> set = new HashSet<Triple>();
        try {
            for (int i = 0; i < numSubjects; i++) {
                set.addAll(factory.getNextSet());
                subjectsInTransaction++;
                if (subjectsInTransaction == subjectsPerTransaction) {
                    add(conn, set);
                    set.clear();
                    subjectsInTransaction = 0;
                }
            }
            if (set.size() > 0) {
                add(conn, set);
            }
        } finally {
            conn.close();
        }
        LOG.info("Finished load test");
    }

    private void add(Connection conn, Set<Triple> set) throws Exception {
        conn.setAutoCommit(false);
        try {
            LOG.info("Adding " + set.size() + " triples on a transaction");
            _adaptor.addTriples(conn, set.iterator());
            conn.commit();
        } catch (Exception e) {
            conn.rollback();
        }
    }

    @After
    public void tearDown() {
    }
            
}
