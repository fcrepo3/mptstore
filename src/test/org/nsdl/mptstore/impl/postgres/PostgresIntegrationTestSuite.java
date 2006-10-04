package org.nsdl.mptstore.impl.postgres;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.swingui.TestRunner;

public class PostgresIntegrationTestSuite extends TestCase {

    public static Test suite() throws Exception {

        TestSuite suite = new TestSuite(PostgresIntegrationTestSuite.class.getName());
   
        // classes in this package
        suite.addTestSuite(PostgresDDLGeneratorIntegrationTest.class);

        return suite;
    }

    public static void main(String[] args) throws Exception {
        if (System.getProperty("text") != null && System.getProperty("text").equals("true")) {
            junit.textui.TestRunner.run(PostgresIntegrationTestSuite.suite());
        } else {
            TestRunner.run(PostgresIntegrationTestSuite.class);
        }
    }
}
