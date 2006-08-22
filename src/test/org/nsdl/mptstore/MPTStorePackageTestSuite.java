package org.nsdl.mptstore;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.swingui.TestRunner;

import org.nsdl.mptstore.core.CorePackageTestSuite;
import org.nsdl.mptstore.impl.ImplPackageTestSuite;
import org.nsdl.mptstore.query.QueryPackageTestSuite;

public class MPTStorePackageTestSuite extends TestCase {

    public static Test suite() throws Exception {

        TestSuite suite = new TestSuite(MPTStorePackageTestSuite.class.getName());
   
        // classes in this package
        //suite.addTestSuite(Whatever.class);

        // sub-package suites
        suite.addTest(CorePackageTestSuite.suite());
        suite.addTest(ImplPackageTestSuite.suite());
        suite.addTest(QueryPackageTestSuite.suite());

        return suite;

    }

    public static void main(String[] args) throws Exception {
        if (System.getProperty("text") != null && System.getProperty("text").equals("true")) {
            junit.textui.TestRunner.run(MPTStorePackageTestSuite.suite());
        } else {
            TestRunner.run(MPTStorePackageTestSuite.class);
        }
    }
}
