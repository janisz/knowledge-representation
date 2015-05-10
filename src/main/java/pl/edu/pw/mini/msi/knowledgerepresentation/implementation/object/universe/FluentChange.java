package pl.edu.pw.mini.msi.knowledgerepresentation.implementation.object.universe;

import pl.edu.pw.mini.msi.knowledgerepresentation.data.Fluent;
/**
 * 
 * @author maq
 *
 */
public class FluentChange extends UniverseObject{
	
	private Fluent fluent;
	private boolean value;
	
	public FluentChange(Fluent fluent, boolean value) {
		this.fluent = fluent;
		this.value = value;
	}
	
	public Fluent getFluent() {
		return fluent;
	}
	public boolean isValue() {
		return value;
	}
	
	public boolean equals(Object object) {
		return object != null && object instanceof FluentChange ? ((FluentChange)object).getId() == id : false;
	}
	
	public int hashCode() {
		return id;
	}
	
}
