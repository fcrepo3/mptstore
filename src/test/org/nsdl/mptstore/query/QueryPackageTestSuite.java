package org.nsdl.mptstore.query;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.swingui.TestRunner;

public class QueryPackageTestSuite extends TestCase {

    public static Test suite() throws Exception {

        TestSuite suite = new TestSuite(QueryPackageTestSuite.class.getName());
   
        // classes in this package
        suite.addTestSuite(SPOQueryCompilerTest.class);

        return suite;

    }

    public static void main(String[] args) throws Exception {
        if (System.getProperty("text") != null && System.getProperty("text").equals("true")) {
            junit.textui.TestRunner.run(QueryPackageTestSuite.suite());
        } else {
            TestRunner.run(QueryPackageTestSuite.class);
        }
    }
}
