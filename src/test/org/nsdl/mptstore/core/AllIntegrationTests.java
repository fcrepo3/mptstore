package org.nsdl.mptstore.core;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    GenericDatabaseAdaptorIntegrationTest.class,
    DDLGeneratorIntegrationTest.class
})
public class AllIntegrationTests { }
