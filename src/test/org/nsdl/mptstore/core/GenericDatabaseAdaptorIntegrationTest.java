package org.nsdl.mptstore.core;

import javax.sql.DataSource;

import junit.framework.TestCase;
import junit.swingui.TestRunner;

import org.nsdl.mptstore.TestConfig;

public class GenericDatabaseAdaptorIntegrationTest 
        extends DatabaseAdaptorIntegrationTest {

    public GenericDatabaseAdaptorIntegrationTest(String name) { 
        super(name);
    }

    public DatabaseAdaptor initAdaptor(DataSource dataSource,
                                       String mapTable,
                                       String soTablePrefix) throws Exception {

        TableManager tableManager = 
                new BasicTableManager(dataSource, 
                                      TestConfig.getDDLGenerator(),
                                      mapTable,
                                      soTablePrefix);

        return new GenericDatabaseAdaptor(tableManager,
                                          TestConfig.getBackslashIsEscape());

    }

    public static void main(String[] args) {
        TestRunner.run(GenericDatabaseAdaptorIntegrationTest.class);
    }   

}
