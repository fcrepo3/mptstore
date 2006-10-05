package org.nsdl.mptstore.rdf;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.swingui.TestRunner;

public class RDFUnitTestSuite extends TestCase {

    public static Test suite() throws Exception {

        TestSuite suite = new TestSuite(RDFUnitTestSuite.class.getName());
   
        // classes in this package
        suite.addTestSuite(LiteralUnitTest.class);

        return suite;

    }

    public static void main(String[] args) throws Exception {
        if (System.getProperty("text") != null && System.getProperty("text").equals("true")) {
            junit.textui.TestRunner.run(RDFUnitTestSuite.suite());
        } else {
            TestRunner.run(RDFUnitTestSuite.class);
        }
    }
}
