package org.nsdl.mptstore.core;

import javax.sql.DataSource;

import org.nsdl.mptstore.TestConfig;

public class GenericDatabaseAdaptorIntegrationTest 
        extends DatabaseAdaptorIntegrationTest {

    private static DatabaseAdaptor ADAPTOR;

    public synchronized DatabaseAdaptor getAdaptor(DataSource dataSource,
            String mapTable, String soTablePrefix)
            throws Exception {
        if (ADAPTOR == null) {
            ADAPTOR = initAdaptor(dataSource, mapTable, soTablePrefix);
        }
        return ADAPTOR;
    }

    private static DatabaseAdaptor initAdaptor(DataSource dataSource,
            String mapTable, String soTablePrefix)
            throws Exception {

        TableManager tableManager = 
                new BasicTableManager(dataSource, 
                                      TestConfig.getDDLGenerator(),
                                      mapTable,
                                      soTablePrefix);

        return new GenericDatabaseAdaptor(tableManager,
                                          TestConfig.getBackslashIsEscape());

    }

}
