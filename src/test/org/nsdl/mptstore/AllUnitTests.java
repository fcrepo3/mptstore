package org.nsdl.mptstore;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    org.nsdl.mptstore.core.AllUnitTests.class,
    org.nsdl.mptstore.impl.AllUnitTests.class,
    org.nsdl.mptstore.query.AllUnitTests.class,
    org.nsdl.mptstore.rdf.AllUnitTests.class,
    org.nsdl.mptstore.util.AllUnitTests.class
})
public class AllUnitTests { }
