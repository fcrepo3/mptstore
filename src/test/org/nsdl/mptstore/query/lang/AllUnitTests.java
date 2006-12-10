package org.nsdl.mptstore.query.lang;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AllUnitTests extends TestCase {

    public static Test suite() throws Exception {

        TestSuite suite = new TestSuite(AllUnitTests.class.getName());
   
        // classes in this package
        //suite.addTestSuite(SomeTest.class);

        // sub-package suites
        suite.addTest(org.nsdl.mptstore.query.lang.spo.AllUnitTests.suite());

        return suite;

    }

}
