package org.nsdl.mptstore;

import java.io.File;
import java.io.FileInputStream;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;

import org.nsdl.mptstore.core.DDLGenerator;
import org.nsdl.mptstore.impl.derby.DerbyDDLGenerator;
import org.nsdl.mptstore.impl.h2.H2DDLGenerator;
import org.nsdl.mptstore.impl.postgres.PostgresDDLGenerator;

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
     * Import system properties from ${test.dir}/test.properties
     * and set derby.system.home to ${test.dir}/derby.
     */
    private static void init() {

        if (!_initialized) {

            try {

                File testPropFile = new File(getTestDir(), "test.properties");
                System.getProperties().load(new FileInputStream(testPropFile));

                System.setProperty("derby.system.home", 
                                   new File(getTestDir(), "derby").getPath());

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

}