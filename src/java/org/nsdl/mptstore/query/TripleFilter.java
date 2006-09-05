package org.nsdl.mptstore.query;

/** Constrains the value of a particular triple pattern node
 * <p>
 * TODO: Make this more straightforward.  Technically, this constrains the
 * value of a single triple pattern node, however as written, it seems like 
 * it would apply to the entire triple.  Need to determine this class's final form
 * <p>
 * @author birkland
 *
 */
public class TripleFilter {
    public final TriplePatternNode s;
    public final TriplePatternNode o;
    public final String operator;

    public TripleFilter(String subject, String operator, String object) {
        this.s = new TriplePatternNode(subject, TriplePatternNode.Types.subject);
        this.o = new TriplePatternNode(object, TriplePatternNode.Types.object);
        this.operator = operator;
    }
}
