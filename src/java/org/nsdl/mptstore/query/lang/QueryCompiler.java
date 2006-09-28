package org.nsdl.mptstore.query.lang;

import org.nsdl.mptstore.query.QueryException;
import org.nsdl.mptstore.query.provider.SQLProvider;

public interface QueryCompiler {

    public SQLProvider compile(String queryText) throws QueryException;

}
