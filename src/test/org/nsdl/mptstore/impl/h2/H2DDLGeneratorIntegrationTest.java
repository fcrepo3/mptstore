package org.nsdl.mptstore.impl.h2;

import junit.framework.TestCase;
import junit.swingui.TestRunner;

import org.nsdl.mptstore.TestConfig;
import org.nsdl.mptstore.core.DDLGeneratorIntegrationTest;

public class H2DDLGeneratorIntegrationTest 
        extends DDLGeneratorIntegrationTest {

    static {
        TestConfig.init();
    }

    public H2DDLGeneratorIntegrationTest(String name) { 
        super(name, H2DDLGenerator.class.getName());
    }

    public static void main(String[] args) {
        TestRunner.run(H2DDLGeneratorIntegrationTest.class);
    }   

}
