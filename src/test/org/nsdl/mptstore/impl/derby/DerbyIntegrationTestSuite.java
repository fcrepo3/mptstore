package org.nsdl.mptstore.impl.derby;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.swingui.TestRunner;

public class DerbyIntegrationTestSuite extends TestCase {

    public static Test suite() throws Exception {

        TestSuite suite = new TestSuite(DerbyIntegrationTestSuite.class.getName());
   
        // classes in this package
        suite.addTestSuite(DerbyDDLGeneratorIntegrationTest.class);

        return suite;
    }

    public static void main(String[] args) throws Exception {
        if (System.getProperty("text") != null && System.getProperty("text").equals("true")) {
            junit.textui.TestRunner.run(DerbyIntegrationTestSuite.suite());
        } else {
            TestRunner.run(DerbyIntegrationTestSuite.class);
        }
    }
}
