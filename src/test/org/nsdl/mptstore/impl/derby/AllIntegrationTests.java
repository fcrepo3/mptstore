package org.nsdl.mptstore.impl.derby;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.swingui.TestRunner;

public class AllIntegrationTests extends TestCase {

    public static Test suite() throws Exception {

        TestSuite suite = new TestSuite(AllIntegrationTests.class.getName());
   
        // classes in this package
        suite.addTestSuite(DerbyDDLGeneratorIntegrationTest.class);

        return suite;
    }

}
