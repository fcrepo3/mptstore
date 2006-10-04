package org.nsdl.mptstore.impl;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.swingui.TestRunner;

import org.nsdl.mptstore.impl.derby.DerbyUnitTestSuite;
import org.nsdl.mptstore.impl.postgres.PostgresUnitTestSuite;

public class ImplUnitTestSuite extends TestCase {

    public static Test suite() throws Exception {

        TestSuite suite = new TestSuite(ImplUnitTestSuite.class.getName());
   
        // classes in this package
        //suite.addTestSuite(Whatever.class);

        // sub-package suites
        suite.addTest(DerbyUnitTestSuite.suite());
        suite.addTest(PostgresUnitTestSuite.suite());

        return suite;

    }

    public static void main(String[] args) throws Exception {
        if (System.getProperty("text") != null && System.getProperty("text").equals("true")) {
            junit.textui.TestRunner.run(ImplUnitTestSuite.suite());
        } else {
            TestRunner.run(ImplUnitTestSuite.class);
        }
    }
}
