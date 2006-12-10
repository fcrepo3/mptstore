package org.nsdl.mptstore.core;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

import org.nsdl.mptstore.TestConfig;

public abstract class DDLGeneratorUnitTest {

    private String _className;

    public static void setUpClass() {
        TestConfig.init();
    }

    protected DDLGeneratorUnitTest(String className) { 
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

    @Test
    public void testGetCreateMapTableDDL() {
        List<String> ddl = getInstance().getCreateMapTableDDL("tableName");
        assertTrue(_className + ".getCreateMapTableDDL returned empty list", 
                ddl.size() > 0);
    }
       
    @Test
    public void testGetDropMapTableDDL() {
        List<String> ddl = getInstance().getDropMapTableDDL("tableName");
        assertTrue(_className + ".getDropMapTableDDL returned empty list", 
                ddl.size() > 0);
    }
    
    @Test
    public void testGetCreateSOTableDDL() {
        List<String> ddl = getInstance().getCreateSOTableDDL("tableName");
        assertTrue(_className + ".getCreateSOTableDDL returned empty list", 
                ddl.size() > 0);
    }
    
    @Test
    public void testGetDropSOTableDDL() {
        List<String> ddl = getInstance().getDropSOTableDDL("tableName");
        assertTrue(_className + ".getDropSOTableDDL returned empty list", 
                ddl.size() > 0);
    }
            
}
