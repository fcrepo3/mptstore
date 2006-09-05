package org.nsdl.mptstore.query;

/** Represents a single 'node' (subject, predicate, object) of a triple pattern
 * <p>
 * Since a triple pattern may be mapped ap a specific table in the MPT model,
 * a triple pattern node, then, can retain a mapping to a specific column
 * of a specific table.  A triple pattern node, then, has the following
 * properties:
 * <ul>
 *  <li> It may be a varible, literal, or URI </li>
 *  <li> It may be mapped to a specific column of a specific table </li>
 *  <li> If it is a variable, there exists a mapping between the variable name 
 *   and its column name </li>
 * </ul>
 * 
 * </p>
 * TODO: See if we can incorporate data typing
 * <p>
 * </p>
 * @author birkland
 *
 */
public class TriplePatternNode {
	private final String value;
	private MPTable boundTable;
	private final String type;
	
	public TriplePatternNode(String value, String type) {
		this.value = value;
		this.type = type;
	}
		
    /* 
     * XXX: Not entirely sure these signatures have the desired
     * extensibility.
     */
    
	public boolean isLiteral() {
		return value.matches("^\".+\"$");
	}
		
	public boolean isURI() {
		return value.matches("^<.+>$");
	}
		
	public boolean isVariable() {
		return value.matches("^\\$.+$");
	}
	
	public void bindTo(MPTable t) {
		this.boundTable = t;
	}
	
	/*
	 * Return the table/alias and column identifier of the RDBMS location of this
	 * value or literal
	 */
	public String mappedName() {
		if (boundTable == null) {
			throw new RuntimeException("Variable " + value + "has not been bound");
		}
		
		if (this.type.equals(TriplePatternNode.Types.predicate)) {
			return boundTable.alias();
		} else {
			return (boundTable.alias() + "." + type);
		}
	}
	
	public MPTable boundTable() {
		return boundTable;
	}
	
	public String value() {
		return value;
	}
	
    public boolean equals(Object p) {
        if (!(p instanceof TriplePatternNode)) {return false;}
        TriplePatternNode comparison = (TriplePatternNode) p;
        
        return comparison.value().equals(this.value());
    
    }
    
	public static class Types {
		public static final String subject = "s";
		public static final String predicate = "p";
		public static final String object = "o";
	}
}
