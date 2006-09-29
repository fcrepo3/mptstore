package org.nsdl.mptstore.query.lang;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.swingui.TestRunner;

import org.nsdl.mptstore.query.lang.spo.SPOPackageTestSuite;

public class LangPackageTestSuite extends TestCase {

    public static Test suite() throws Exception {

        TestSuite suite = new TestSuite(LangPackageTestSuite.class.getName());
   
        // classes in this package
        //suite.addTestSuite(SomeTest.class);

        // sub-package suites
        suite.addTest(SPOPackageTestSuite.suite());

        return suite;

    }

    public static void main(String[] args) throws Exception {
        if (System.getProperty("text") != null && System.getProperty("text").equals("true")) {
            junit.textui.TestRunner.run(LangPackageTestSuite.suite());
        } else {
            TestRunner.run(LangPackageTestSuite.class);
        }
    }
}
