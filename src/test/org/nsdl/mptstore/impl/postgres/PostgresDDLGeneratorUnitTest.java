package org.nsdl.mptstore.impl.postgres;

import junit.framework.TestCase;
import junit.swingui.TestRunner;

import org.nsdl.mptstore.core.DDLGeneratorUnitTest;

public class PostgresDDLGeneratorUnitTest extends DDLGeneratorUnitTest {

    public PostgresDDLGeneratorUnitTest(String name) { 
        super(name, PostgresDDLGenerator.class.getName());
    }

    public void setUp() {
    }
            
    public void tearDown() {
    }

    public static void main(String[] args) {
        TestRunner.run(PostgresDDLGeneratorUnitTest.class);
    }   

}
