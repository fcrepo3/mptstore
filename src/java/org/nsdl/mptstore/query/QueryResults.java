package org.nsdl.mptstore.query;

import java.util.List;

import org.nsdl.mptstore.core.ClosableIterator;

public interface QueryResults extends ClosableIterator<List<String>> {

    public List<String> getTargets();

}
