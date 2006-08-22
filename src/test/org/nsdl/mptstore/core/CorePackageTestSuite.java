package org.nsdl.mptstore.core;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.swingui.TestRunner;

public class CorePackageTestSuite extends TestCase {

    public static Test suite() throws Exception {

        TestSuite suite = new TestSuite(CorePackageTestSuite.class.getName());
   
        // classes in this package
        //suite.addTestSuite(Whatever.class);

        return suite;

    }

    public static void main(String[] args) throws Exception {
        if (System.getProperty("text") != null && System.getProperty("text").equals("true")) {
            junit.textui.TestRunner.run(CorePackageTestSuite.suite());
        } else {
            TestRunner.run(CorePackageTestSuite.class);
        }
    }
}
