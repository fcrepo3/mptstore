package org.nsdl.mptstore.query;

import java.util.List;

public interface SQLBuilder extends SQLProvider {

    public void setTargets(List<String> targets);

    // public void setQuery(GraphQuery q);
    // public void addRequired(QueryElement e);
    // public void addOptional(QueryElement e);
}
