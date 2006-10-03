package org.nsdl.mptstore.query.component;


/** A component of a graph query.
 * <p>
 * Currently, query elements may be a {@link GraphQuery} or a 
 * {@link GraphPattern}.  The known QueryElement types are 
 * enumerated in {@link QueryElement.Type}.
 * </p>
 * 
 * @author birkland
 *
 */
public interface QueryElement {

    /**
     * Get the type of this element.
     *
     * @return the type.
     */
    Type getType();

    /**
     * Possible element types.
     */
    enum Type {

        /** A graph query. */
        GraphQuery,

        /** A graph pattern. */
        GraphPattern
    }
}
