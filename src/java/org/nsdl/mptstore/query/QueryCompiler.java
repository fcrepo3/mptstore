package org.nsdl.mptstore.query;

public interface QueryCompiler {

    public SQLProvider compile(String queryText) throws QueryException;

}
