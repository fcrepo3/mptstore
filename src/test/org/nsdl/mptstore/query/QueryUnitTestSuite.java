package org.nsdl.mptstore.query;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.swingui.TestRunner;

import org.nsdl.mptstore.query.lang.LangUnitTestSuite;

public class QueryUnitTestSuite extends TestCase {

    public static Test suite() throws Exception {

        TestSuite suite = new TestSuite(QueryUnitTestSuite.class.getName());
   
        // classes in this package
        //suite.addTestSuite(SomeTest.class);

        // sub-package suites
        suite.addTest(LangUnitTestSuite.suite());

        return suite;

    }

    public static void main(String[] args) throws Exception {
        if (System.getProperty("text") != null && System.getProperty("text").equals("true")) {
            junit.textui.TestRunner.run(QueryUnitTestSuite.suite());
        } else {
            TestRunner.run(QueryUnitTestSuite.class);
        }
    }
}
