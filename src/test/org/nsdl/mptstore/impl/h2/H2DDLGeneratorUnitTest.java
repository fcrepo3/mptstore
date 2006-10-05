package org.nsdl.mptstore.impl.h2;

import junit.framework.TestCase;
import junit.swingui.TestRunner;

import org.nsdl.mptstore.core.DDLGeneratorUnitTest;

public class H2DDLGeneratorUnitTest extends DDLGeneratorUnitTest {

    public H2DDLGeneratorUnitTest(String name) { 
        super(name, H2DDLGenerator.class.getName());
    }

    public void setUp() {
    }
            
    public void tearDown() {
    }

    public static void main(String[] args) {
        TestRunner.run(H2DDLGeneratorUnitTest.class);
    }   

}
