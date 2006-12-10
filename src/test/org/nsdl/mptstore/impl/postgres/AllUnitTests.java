package org.nsdl.mptstore.impl.postgres;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.swingui.TestRunner;

public class AllUnitTests extends TestCase {

    public static Test suite() throws Exception {

        TestSuite suite = new TestSuite(AllUnitTests.class.getName());
   
        // classes in this package
        suite.addTestSuite(PostgresDDLGeneratorUnitTest.class);

        return suite;

    }

}
