package org.nsdl.mptstore.query.lang.spo;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.swingui.TestRunner;

import org.nsdl.mptstore.query.lang.spo.AllUnitTests;

public class AllUnitTests extends TestCase {

    public static Test suite() throws Exception {

        TestSuite suite = new TestSuite(AllUnitTests.class.getName());
   
        // classes in this package
        suite.addTestSuite(SPOQueryCompilerUnitTest.class);

        return suite;

    }

}
