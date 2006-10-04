package org.nsdl.mptstore.impl.postgres;

import junit.framework.TestCase;
import junit.swingui.TestRunner;

import org.nsdl.mptstore.core.DDLGeneratorIntegrationTest;

public class PostgresDDLGeneratorIntegrationTest 
        extends DDLGeneratorIntegrationTest {

    public PostgresDDLGeneratorIntegrationTest(String name) { 
        super(name, PostgresDDLGenerator.class.getName());
    }

    public static void main(String[] args) {
        TestRunner.run(PostgresDDLGeneratorIntegrationTest.class);
    }   

}
