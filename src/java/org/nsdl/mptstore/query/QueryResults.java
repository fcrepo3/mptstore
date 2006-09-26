package org.nsdl.mptstore.query;

import java.util.List;

import org.nsdl.mptstore.core.ClosableIterator;
import org.nsdl.mptstore.rdf.Node;

public interface QueryResults extends ClosableIterator<List<Node>> {

    public List<String> getTargets();

}
