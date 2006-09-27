package org.nsdl.mptstore.query;

import java.util.List;

public interface SQLBuilder extends SQLProvider {

    public void setTargets(List<String> targets);

}
