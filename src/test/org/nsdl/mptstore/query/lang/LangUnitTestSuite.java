package org.nsdl.mptstore.query.lang;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.swingui.TestRunner;

import org.nsdl.mptstore.query.lang.spo.SPOUnitTestSuite;

public class LangUnitTestSuite extends TestCase {

    public static Test suite() throws Exception {

        TestSuite suite = new TestSuite(LangUnitTestSuite.class.getName());
   
        // classes in this package
        //suite.addTestSuite(SomeTest.class);

        // sub-package suites
        suite.addTest(SPOUnitTestSuite.suite());

        return suite;

    }

    public static void main(String[] args) throws Exception {
        if (System.getProperty("text") != null && System.getProperty("text").equals("true")) {
            junit.textui.TestRunner.run(LangUnitTestSuite.suite());
        } else {
            TestRunner.run(LangUnitTestSuite.class);
        }
    }
}
