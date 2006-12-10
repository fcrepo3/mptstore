package org.nsdl.mptstore;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.swingui.TestRunner;

public class AllIntegrationTests extends TestCase {

    public static Test suite() throws Exception {

        TestSuite suite = new TestSuite(AllIntegrationTests.class.getName());
   
        // classes in this package
        //suite.addTestSuite(Whatever.class);

        // sub-package suites
        suite.addTest(org.nsdl.mptstore.core.AllIntegrationTests.suite());
        suite.addTest(org.nsdl.mptstore.impl.AllIntegrationTests.suite());

        return suite;

    }

}
