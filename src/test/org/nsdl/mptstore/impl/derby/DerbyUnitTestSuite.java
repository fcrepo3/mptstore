package org.nsdl.mptstore.impl.derby;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.swingui.TestRunner;

public class DerbyUnitTestSuite extends TestCase {

    public static Test suite() throws Exception {

        TestSuite suite = new TestSuite(DerbyUnitTestSuite.class.getName());
   
        // classes in this package
        suite.addTestSuite(DerbyDDLGeneratorUnitTest.class);

        return suite;
    }

    public static void main(String[] args) throws Exception {
        if (System.getProperty("text") != null && System.getProperty("text").equals("true")) {
            junit.textui.TestRunner.run(DerbyUnitTestSuite.suite());
        } else {
            TestRunner.run(DerbyUnitTestSuite.class);
        }
    }
}
