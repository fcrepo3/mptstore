package org.nsdl.mptstore.impl.derby;

import junit.framework.TestCase;
import junit.swingui.TestRunner;

import org.nsdl.mptstore.TestConfig;
import org.nsdl.mptstore.core.DDLGeneratorUnitTest;

public class DerbyDDLGeneratorUnitTest extends DDLGeneratorUnitTest {

    static {
        TestConfig.init();
    }

    public DerbyDDLGeneratorUnitTest(String name) { 
        super(name, DerbyDDLGenerator.class.getName());
    }

    public void setUp() {
    }
            
    public void tearDown() {
    }

    public static void main(String[] args) {
        TestRunner.run(DerbyDDLGeneratorUnitTest.class);
    }   

}
