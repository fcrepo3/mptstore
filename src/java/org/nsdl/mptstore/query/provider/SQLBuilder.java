package org.nsdl.mptstore.query.provider;

import java.util.List;

public interface SQLBuilder extends SQLProvider {

    public void setTargets(List<String> targets);

}
