package org.nsdl.mptstore.query;

import java.util.List;

public interface SQLProvider {

    public List<String> getTargets();

    public List<String> getSQL() throws QueryException;

}
