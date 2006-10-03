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
     * @param targets the targets.
     * @see SQLProvider#getTargets()
     */
    void setTargets(List<String> targets);

}
