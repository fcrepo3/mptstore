package org.nsdl.mptstore.impl.postgres;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.swingui.TestRunner;

public class PostgresUnitTestSuite extends TestCase {

    public static Test suite() throws Exception {

        TestSuite suite = new TestSuite(PostgresUnitTestSuite.class.getName());
   
        // classes in this package
        //suite.addTestSuite(Whatever.class);

        return suite;

    }

    public static void main(String[] args) throws Exception {
        if (System.getProperty("text") != null && System.getProperty("text").equals("true")) {
            junit.textui.TestRunner.run(PostgresUnitTestSuite.suite());
        } else {
            TestRunner.run(PostgresUnitTestSuite.class);
        }
    }
}
