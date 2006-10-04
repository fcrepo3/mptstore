package org.nsdl.mptstore.core;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.swingui.TestRunner;

public class CoreIntegrationTestSuite extends TestCase {

    public static Test suite() throws Exception {

        TestSuite suite = new TestSuite(CoreIntegrationTestSuite.class.getName());
   
        // classes in this package
        suite.addTestSuite(GenericDatabaseAdaptorIntegrationTest.class);

        return suite;

    }

    public static void main(String[] args) throws Exception {
        if (System.getProperty("text") != null && System.getProperty("text").equals("true")) {
            junit.textui.TestRunner.run(CoreIntegrationTestSuite.suite());
        } else {
            TestRunner.run(CoreIntegrationTestSuite.class);
        }
    }
}
