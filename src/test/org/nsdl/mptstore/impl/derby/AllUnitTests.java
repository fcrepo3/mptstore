package org.nsdl.mptstore.impl.derby;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.swingui.TestRunner;

public class AllUnitTests extends TestCase {

    public static Test suite() throws Exception {

        TestSuite suite = new TestSuite(AllUnitTests.class.getName());
   
        // classes in this package
        suite.addTestSuite(DerbyDDLGeneratorUnitTest.class);

        return suite;
    }

}
