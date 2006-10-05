package org.nsdl.mptstore.impl.h2;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.swingui.TestRunner;

public class H2UnitTestSuite extends TestCase {

    public static Test suite() throws Exception {

        TestSuite suite = new TestSuite(H2UnitTestSuite.class.getName());
   
        // classes in this package
        suite.addTestSuite(H2DDLGeneratorUnitTest.class);

        return suite;
    }

    public static void main(String[] args) throws Exception {
        if (System.getProperty("text") != null && System.getProperty("text").equals("true")) {
            junit.textui.TestRunner.run(H2UnitTestSuite.suite());
        } else {
            TestRunner.run(H2UnitTestSuite.class);
        }
    }
}
