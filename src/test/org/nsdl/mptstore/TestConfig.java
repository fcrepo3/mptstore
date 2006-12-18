package org.nsdl.mptstore;

import java.io.File;
import java.io.FileInputStream;

import java.util.Properties;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;

import org.apache.log4j.PropertyConfigurator;

import org.nsdl.mptstore.core.BasicTableManager;
import org.nsdl.mptstore.core.DatabaseAdaptor;
import org.nsdl.mptstore.core.DDLGenerator;
import org.nsdl.mptstore.core.GenericDatabaseAdaptor;
import org.nsdl.mptstore.core.TableManager;
import org.nsdl.mptstore.impl.derby.DerbyDDLGenerator;
import org.nsdl.mptstore.impl.h2.H2DDLGenerator;
import org.nsdl.mptstore.impl.postgres.PostgresDDLGenerator;

import org.nsdl.mptstore.perftest.TestTripleFactory;

/**
 * Provides static access to test configuration.
 *
 * The source of the test configuration is the <code>test.properties</code>
 * file, which is exprected to be located in the directory indicated by 
 * the <code>test.dir</code> system property.
 *
 * @author cwilper@cs.cornell.edu
 */
public abstract class TestConfig {

    private static boolean _initialized = false;

    private TestConfig() { }

    /**
     * Imports system properties from ${test.dir}/test.properties,
     * sets derby.system.home to ${test.dir}/derby,
     * and initializes logging
     */
    public static void init() {

        if (!_initialized) {

            try {

                // put everything in test.properties into system properties
                File testPropFile = new File(getTestDir(), "test.properties");
                System.getProperties().load(new FileInputStream(testPropFile));

                // tell derby where to store stuff 
                System.setProperty("derby.system.home", 
                                   new File(getTestDir(), "derby").getPath());

                // configure log4j
                final String rootPackage = "org.nsdl.mptstore";
                Properties logProps = new Properties();
                logProps.setProperty("log4j.appender.TESTLOG", 
                        "org.apache.log4j.FileAppender");
                logProps.setProperty("log4j.appender.TESTLOG.File", 
                        new File(getTestDir(), "test.log").getPath());
                logProps.setProperty("log4j.appender.TESTLOG.layout",
                        "org.apache.log4j.PatternLayout");
                logProps.setProperty("log4j.appender.TESTLOG.layout.ConversionPattern",
                        "%p %d{yyyy-MM-dd' 'HH:mm:ss.SSS} [%t] (%c{1}) %m%n");
                logProps.setProperty("log4j.rootLogger", "WARN, TESTLOG");
                logProps.setProperty("log4j.logger." + rootPackage, 
                        System.getProperty("test.loglevel", "INFO")
                        + ", TESTLOG");
                logProps.setProperty("log4j.additivity." + rootPackage,
                        "false");
                PropertyConfigurator.configure(logProps);

                // tell commons-logging to use log4j
                System.setProperty("org.apache.commons.logging.LogFactory",
                        "org.apache.commons.logging.impl.Log4jFactory");
                System.setProperty("org.apache.commons.logging.Log",
                        "org.apache.commons.logging.impl.Log4JLogger");

                _initialized = true;

            } catch (Throwable th) {
                throw new RuntimeException("Test initialization failed", th);
            }

        }

    }

    /**
     * Get a system property value, or <code>null</code> if not required
     * and undefined.
     */
    private static String getProp(String name, boolean required) {
        String value = System.getProperty(name);
        if (required && (value == null || value.equals(""))) {
            throw new RuntimeException("Property must be defined: " + name);
        } else {
            return value;
        }
    }

    /**
     * Get a required system property as an <code>int</code> value.
     */
    private static int getIntProp(String name) {
        String value = getProp(name, true);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Property must be an int value: " 
                    + name);
        }
    }

    /**
     * Get the test directory, as defined by the <code>test.dir</code>
     * system property.
     */
    public static File getTestDir() {
        String testDir = getProp("test.dir", true);
        return new File(testDir);
    }

    /**
     * Get the name of the database we're testing.
     *
     * This is the value of the <code>test.database</code> system property.
     * For this NAME, the following system properties should also be defined:
     *
     * <ul>
     *   <li> <code>test.NAME.driver</code></li>
     *   <li> <code>test.NAME.url</code></li>
     *   <li> <code>test.NAME.username</code></li>
     *   <li> <code>test.NAME.password</code></li>
     * </ul>
     */
    public static String getTestDatabase() {
        init();
        return getProp("test.database", true);
    }

    /**
     * Get the DDLGenerator appropriate for <code>test.database</code>.
     */
    public static DDLGenerator getDDLGenerator() {
        init();
        String database = getTestDatabase();
        if (database.equals("derby")) {
            return new DerbyDDLGenerator();
        } else if (database.equals("h2")) {
            return new H2DDLGenerator();
        } else if (database.equals("postgres")) {
            return new PostgresDDLGenerator();
        } else {
            throw new RuntimeException("No known DDLGenerator for database: "
                    + database);
        }
    }

    /**
     * Tell whether backslash should be treated as an escape character
     * for <code>test.database</code>.
     */
    public static boolean getBackslashIsEscape() {
        init();
        String database = getTestDatabase();
        if (database.equals("derby")) {
            return false; // same for oracle, btw
        } else if (database.equals("h2")) {
            return false;
        } else if (database.equals("postgres")) {
            return true;  // same for mysql, btw
        } else {
            throw new RuntimeException("Unrecognized test database: "
                    + database);
        }
    }

    /**
     * Get a reasonable default fetch size for the database we're
     * testing.
     */
    public static int getFetchSize() {
        init();
        String database = getTestDatabase();
        if (database.equals("derby")) {
            return 1000;
        } else if (database.equals("h2")) {
            return 1000;
        } else if (database.equals("postgres")) {
            return 1000;
        } else {
            throw new RuntimeException("Unrecognized test database: "
                    + database);
        }
    }

    /**
     * Get a JDBC DataSource based on the test configuration properties.
     */
    public static DataSource getTestDataSource(int maxActive) {
        init();
        try {
            String database = getTestDatabase();
            String driver   = getProp("test." + database + ".driver", true);
            String url      = getProp("test." + database + ".url", true);
            String username = getProp("test." + database + ".username", true);
            String password = getProp("test." + database + ".password", true);
    
            Properties dbProperties = new Properties();
            dbProperties.setProperty("url", url);
            dbProperties.setProperty("username", username);
            dbProperties.setProperty("password", password);
            dbProperties.setProperty("maxActive", "" + maxActive);

            Class.forName(driver);
    
            BasicDataSource pool = (BasicDataSource)
                    BasicDataSourceFactory.createDataSource(dbProperties);

            pool.setDriverClassName(driver);

            return pool;
        } catch (Throwable th) {
            throw new RuntimeException("Error getting test database "
                    + "connection pool", th);
        }
        
    }

    public static int getNumTestSubjects() {
        init();
        return getIntProp("test.perf.subjects");
    }

    public static int getNumTestSubjectsPerTransaction() {
        init();
        return getIntProp("test.perf.subjectsPerTransaction");
    }

    public static TestTripleFactory getTestTripleFactory() {
        init();
        return new TestTripleFactory(getIntProp("test.perf.rels"), 
                getIntProp("test.perf.plains"), getIntProp("test.perf.locals"), 
                getIntProp("test.perf.longs"), getIntProp("test.perf.doubles"), 
                getIntProp("test.perf.dateTimes"));
    }

    public static DatabaseAdaptor getTestDatabaseAdaptor(
            DataSource dataSource) 
            throws SQLException {
        init();
        TableManager mgr = new BasicTableManager(dataSource, getDDLGenerator(),
                "tMap", "t");
        return new GenericDatabaseAdaptor(mgr, getBackslashIsEscape());
    }

}