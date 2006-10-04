package org.nsdl.mptstore;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.swingui.TestRunner;

import org.nsdl.mptstore.core.CoreUnitTestSuite;
import org.nsdl.mptstore.impl.ImplUnitTestSuite;
import org.nsdl.mptstore.query.QueryUnitTestSuite;
import org.nsdl.mptstore.rdf.RDFUnitTestSuite;
import org.nsdl.mptstore.util.UtilUnitTestSuite;

public class MPTStoreUnitTestSuite extends TestCase {

    public static Test suite() throws Exception {

        TestSuite suite = new TestSuite(MPTStoreUnitTestSuite.class.getName());
   
        // classes in this package
        //suite.addUnitTestSuite(Whatever.class);

        // sub-package suites
        suite.addTest(CoreUnitTestSuite.suite());
        suite.addTest(ImplUnitTestSuite.suite());
        suite.addTest(QueryUnitTestSuite.suite());
        suite.addTest(RDFUnitTestSuite.suite());
        suite.addTest(UtilUnitTestSuite.suite());

        return suite;

    }

    public static void main(String[] args) throws Exception {
        if (System.getProperty("text") != null && System.getProperty("text").equals("true")) {
            junit.textui.TestRunner.run(MPTStoreUnitTestSuite.suite());
        } else {
            TestRunner.run(MPTStoreUnitTestSuite.class);
        }
    }
}
