package org.nsdl.mptstore;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.swingui.TestRunner;

import org.nsdl.mptstore.core.CoreIntegrationTestSuite;
import org.nsdl.mptstore.impl.ImplIntegrationTestSuite;

public class MPTStoreIntegrationTestSuite extends TestCase {

    public static Test suite() throws Exception {

        TestSuite suite = new TestSuite(MPTStoreIntegrationTestSuite.class.getName());
   
        // classes in this package
        //suite.addTestSuite(Whatever.class);

        // sub-package suites
        suite.addTest(CoreIntegrationTestSuite.suite());
        suite.addTest(ImplIntegrationTestSuite.suite());

        return suite;

    }



    public static void main(String[] args) throws Exception {
        if (System.getProperty("text") != null && System.getProperty("text").equals("true")) {
            junit.textui.TestRunner.run(MPTStoreIntegrationTestSuite.suite());
        } else {
            TestRunner.run(MPTStoreIntegrationTestSuite.class);
        }
    }
}
