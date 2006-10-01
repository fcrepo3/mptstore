package org.nsdl.mptstore.query.provider;

import java.util.List;

/**
 * A <code>SQLProvider</code> that supports target setting.
 *
 * @author cwilper@cs.cornell.edu
 */
public interface SQLBuilder extends SQLProvider {

    /**
     * Set the targets to the given values.
     *
     * @see SQLProvider#getTargets()
     */
    public void setTargets(List<String> targets);

}
