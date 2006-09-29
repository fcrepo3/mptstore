package org.nsdl.mptstore.query.lang.spo;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.swingui.TestRunner;

import org.nsdl.mptstore.query.lang.spo.SPOPackageTestSuite;

public class SPOPackageTestSuite extends TestCase {

    public static Test suite() throws Exception {

        TestSuite suite = new TestSuite(SPOPackageTestSuite.class.getName());
   
        // classes in this package
        suite.addTestSuite(SPOQueryCompilerTest.class);

        return suite;

    }

    public static void main(String[] args) throws Exception {
        if (System.getProperty("text") != null && System.getProperty("text").equals("true")) {
            junit.textui.TestRunner.run(SPOPackageTestSuite.suite());
        } else {
            TestRunner.run(SPOPackageTestSuite.class);
        }
    }
}
