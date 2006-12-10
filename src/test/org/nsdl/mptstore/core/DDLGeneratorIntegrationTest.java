package org.nsdl.mptstore.core;

import java.util.List;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.nsdl.mptstore.TestConfig;

public class DDLGeneratorIntegrationTest {

    private static DDLGenerator GENERATOR;

    private static DataSource POOL;

    private Connection _conn;

    @BeforeClass
    public static void setUpClass() {
        TestConfig.init();
        GENERATOR = TestConfig.getDDLGenerator();
        POOL = TestConfig.getTestDataSource(2);
    }

    @Before
    public void setUp() throws Exception {
        _conn = POOL.getConnection();
    }

    @After
    public void tearDown() throws Exception {
        _conn.close();
    }

    @Test
    public void testCreateAndDropMapTable() {
        try {
            executeUpdates(GENERATOR.getCreateMapTableDDL("tMap"));
            executeUpdates(GENERATOR.getDropMapTableDDL("tMap"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCreateAndDropSOTable() {
        try {
            executeUpdates(GENERATOR.getCreateMapTableDDL("t1"));
            executeUpdates(GENERATOR.getDropMapTableDDL("t1"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void executeUpdates(List<String> sql) throws SQLException {

        Statement st = _conn.createStatement();
        try {
            for (String statement : sql) {
                st.executeUpdate(statement); 
            }
        } finally {
            st.close();
        }
    }

}
