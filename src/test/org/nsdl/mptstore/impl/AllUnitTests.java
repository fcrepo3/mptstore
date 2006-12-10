package org.nsdl.mptstore.impl;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    org.nsdl.mptstore.impl.derby.AllUnitTests.class,
    org.nsdl.mptstore.impl.h2.AllUnitTests.class,
    org.nsdl.mptstore.impl.postgres.AllUnitTests.class
})
public class AllUnitTests { }
