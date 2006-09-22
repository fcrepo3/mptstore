package org.nsdl.mptstore.rdf;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.swingui.TestRunner;

public class RDFPackageTestSuite extends TestCase {

    public static Test suite() throws Exception {

        TestSuite suite = new TestSuite(RDFPackageTestSuite.class.getName());
   
        // classes in this package
        suite.addTestSuite(RDFUtilTest.class);

        return suite;

    }

    public static void main(String[] args) throws Exception {
        if (System.getProperty("text") != null && System.getProperty("text").equals("true")) {
            junit.textui.TestRunner.run(RDFPackageTestSuite.suite());
        } else {
            TestRunner.run(RDFPackageTestSuite.class);
        }
    }
}
