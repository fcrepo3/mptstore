package org.nsdl.mptstore.util;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.swingui.TestRunner;

public class UtilUnitTestSuite extends TestCase {

    public static Test suite() throws Exception {

        TestSuite suite = new TestSuite(UtilUnitTestSuite.class.getName());
   
        // classes in this package
        suite.addTestSuite(NTriplesUtilUnitTest.class);

        // sub-package suites

        return suite;

    }

    public static void main(String[] args) throws Exception {
        if (System.getProperty("text") != null && System.getProperty("text").equals("true")) {
            junit.textui.TestRunner.run(UtilUnitTestSuite.suite());
        } else {
            TestRunner.run(UtilUnitTestSuite.class);
        }
    }
}
