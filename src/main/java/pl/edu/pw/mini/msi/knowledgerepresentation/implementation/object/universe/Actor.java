package pl.edu.pw.mini.msi.knowledgerepresentation.implementation.object.universe;
/**
 * 
 * @author maq
 *
 */
public class Actor extends UniverseObject{
	
	private String name;
	
	public Actor(String name) {
		this.name = name;
	}
	
	public boolean equals(Object object) {
		return object != null && object instanceof UniverseObject ? ((UniverseObject)object).getId() == id : false;
	}
	
	public int hashCode() {
		return id;
	}
}
