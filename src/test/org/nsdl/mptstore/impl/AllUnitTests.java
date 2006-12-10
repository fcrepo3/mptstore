package org.nsdl.mptstore.impl;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.swingui.TestRunner;

import org.nsdl.mptstore.TestConfig;

public class AllUnitTests extends TestCase {

    public static Test suite() throws Exception {

        TestSuite suite = new TestSuite(AllUnitTests.class.getName());
   
        // classes in this package
        //suite.addTestSuite(Whatever.class);

        // sub-package suites
        suite.addTest(org.nsdl.mptstore.impl.derby.AllUnitTests.suite());
        suite.addTest(org.nsdl.mptstore.impl.h2.AllUnitTests.suite());
        suite.addTest(org.nsdl.mptstore.impl.postgres.AllUnitTests.suite());

        return suite;

    }

}
