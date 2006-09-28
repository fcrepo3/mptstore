package org.nsdl.mptstore.query.provider;

import java.util.List;

import org.nsdl.mptstore.query.QueryException;

public interface SQLProvider {

    public List<String> getTargets();

    public List<String> getSQL() throws QueryException;

}
