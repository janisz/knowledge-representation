package pl.edu.pw.mini.msi.knowledgerepresentation.implementation.object.universe;
/**
 * 
 * @author maq
 *
 */
public class Fluent extends UniverseObject{
	
	private String name;
	
	public Fluent(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean equals(Object object) {
		return object != null && object instanceof Fluent ? ((Fluent)object).getId() == id : false;
	}
	
	public int hashCode() {
		return id;
	}
	
}
