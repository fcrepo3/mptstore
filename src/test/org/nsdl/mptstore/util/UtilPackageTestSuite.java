package org.nsdl.mptstore.util;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.swingui.TestRunner;

public class UtilPackageTestSuite extends TestCase {

    public static Test suite() throws Exception {

        TestSuite suite = new TestSuite(UtilPackageTestSuite.class.getName());
   
        // classes in this package
        suite.addTestSuite(NTriplesUtilTest.class);

        // sub-package suites

        return suite;

    }

    public static void main(String[] args) throws Exception {
        if (System.getProperty("text") != null && System.getProperty("text").equals("true")) {
            junit.textui.TestRunner.run(UtilPackageTestSuite.suite());
        } else {
            TestRunner.run(UtilPackageTestSuite.class);
        }
    }
}
