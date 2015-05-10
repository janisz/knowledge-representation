package pl.edu.pw.mini.msi.knowledgerepresentation.implementation.object.universe;
/**
 * 
 * @author maq
 *
 */
public class FluentChangeTrigger extends UniverseObject{
	
	public boolean equals(Object object) {
		return object != null && object instanceof ActionInvocation ? ((ActionInvocation)object).getId() == id : false;
	}
	
	public int hashCode() {
		return id;
	}
	
}


