package org.nsdl.mptstore;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.swingui.TestRunner;

public class AllUnitTests extends TestCase {

    public static Test suite() throws Exception {

        TestSuite suite = new TestSuite(AllUnitTests.class.getName());
   
        // classes in this package
        //suite.addUnitTestSuite(Whatever.class);

        // sub-package suites
        suite.addTest(org.nsdl.mptstore.core.AllUnitTests.suite());
        suite.addTest(org.nsdl.mptstore.impl.AllUnitTests.suite());
        suite.addTest(org.nsdl.mptstore.query.AllUnitTests.suite());
        suite.addTest(org.nsdl.mptstore.rdf.AllUnitTests.suite());
        suite.addTest(org.nsdl.mptstore.util.AllUnitTests.suite());

        return suite;

    }
}
