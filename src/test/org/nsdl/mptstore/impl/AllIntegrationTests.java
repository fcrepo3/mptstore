package org.nsdl.mptstore.impl;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.swingui.TestRunner;

import org.nsdl.mptstore.TestConfig;

public class AllIntegrationTests extends TestCase {

    public static Test suite() throws Exception {

        TestSuite suite = new TestSuite(AllIntegrationTests.class.getName());
   
        // classes in this package
        //suite.addTestSuite(Whatever.class);

        // sub-package suites
        String testDatabase = TestConfig.getTestDatabase();

        if (testDatabase.equals("derby")) {
            suite.addTest(org.nsdl.mptstore.impl.derby.AllIntegrationTests.suite());
        } else if (testDatabase.equals("h2")) {
            suite.addTest(org.nsdl.mptstore.impl.h2.AllIntegrationTests.suite());
        } else if (testDatabase.equals("postgres")) {
            suite.addTest(org.nsdl.mptstore.impl.postgres.AllIntegrationTests.suite());
        } else {
            throw new Exception("Unrecognized test.database: " + testDatabase);
        }

        return suite;

    }

}
