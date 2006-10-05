package org.nsdl.mptstore.impl;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.swingui.TestRunner;

import org.nsdl.mptstore.MPTStoreIntegrationTestSuite;
import org.nsdl.mptstore.TestConfig;
import org.nsdl.mptstore.impl.derby.DerbyIntegrationTestSuite;
import org.nsdl.mptstore.impl.h2.H2IntegrationTestSuite;
import org.nsdl.mptstore.impl.postgres.PostgresIntegrationTestSuite;

public class ImplIntegrationTestSuite extends TestCase {

    public static Test suite() throws Exception {

        TestSuite suite = new TestSuite(ImplIntegrationTestSuite.class.getName());
   
        // classes in this package
        //suite.addTestSuite(Whatever.class);

        // sub-package suites
        String testDatabase = TestConfig.getTestDatabase();

        if (testDatabase.equals("derby")) {
            suite.addTest(DerbyIntegrationTestSuite.suite());
        } else if (testDatabase.equals("h2")) {
            suite.addTest(H2IntegrationTestSuite.suite());
        } else if (testDatabase.equals("postgres")) {
            suite.addTest(PostgresIntegrationTestSuite.suite());
        } else {
            throw new Exception("Unrecognized test.database: " + testDatabase);
        }

        return suite;

    }

    public static void main(String[] args) throws Exception {
        if (System.getProperty("text") != null && System.getProperty("text").equals("true")) {
            junit.textui.TestRunner.run(ImplIntegrationTestSuite.suite());
        } else {
            TestRunner.run(ImplIntegrationTestSuite.class);
        }
    }
}
