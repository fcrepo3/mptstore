package org.nsdl.mptstore.core;

import java.util.Iterator;
import java.util.List;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import junit.framework.TestCase;
import junit.swingui.TestRunner;

import org.nsdl.mptstore.TestConfig;

public abstract class DDLGeneratorIntegrationTest extends TestCase {

    private String _className;

    private Connection _conn;

    protected DDLGeneratorIntegrationTest(String name,
                                          String className) { 
        super(name);
        _className = className;
    }

    /**
     * Get an instance of the DDLGenerator we're testing.
     *
     * The DDLGenerator implementation must have a public no-arg
     * constructor.
     */
    protected DDLGenerator getInstance() {
        try {
            return (DDLGenerator) Class.forName(_className).newInstance();
        } catch (Throwable th) {
            throw new RuntimeException("Error getting instance of " 
                    + _className, th);
        }
    }

    public void setUp() throws Exception {
        DataSource pool = TestConfig.getTestDataSource(2);
        _conn = pool.getConnection();
    }

    public void tearDown() throws Exception {
        _conn.close();
    }

    public void testCreateAndDropMapTable() {
        try {
            executeUpdates(getInstance().getCreateMapTableDDL("tMap"));
            executeUpdates(getInstance().getDropMapTableDDL("tMap"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void testCreateAndDropSOTable() {
        try {
            executeUpdates(getInstance().getCreateMapTableDDL("t1"));
            executeUpdates(getInstance().getDropMapTableDDL("t1"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
