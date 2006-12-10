package org.nsdl.mptstore.rdf;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AllUnitTests extends TestCase {

    public static Test suite() throws Exception {

        TestSuite suite = new TestSuite(AllUnitTests.class.getName());
   
        // classes in this package
        suite.addTestSuite(LiteralUnitTest.class);

        return suite;

    }

}
