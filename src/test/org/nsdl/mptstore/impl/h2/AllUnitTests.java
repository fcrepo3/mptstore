package org.nsdl.mptstore.impl.h2;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.swingui.TestRunner;

public class AllUnitTests extends TestCase {

    public static Test suite() throws Exception {

        TestSuite suite = new TestSuite(AllUnitTests.class.getName());
   
        // classes in this package
        suite.addTestSuite(H2DDLGeneratorUnitTest.class);

        return suite;
    }

}
