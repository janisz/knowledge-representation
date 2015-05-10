package pl.edu.pw.mini.msi.knowledgerepresentation.implementation.object.universe;

import pl.edu.pw.mini.msi.knowledgerepresentation.implementation.object.Time;
/**
 * 
 * @author maq
 *
 */
public class ActionInvocation extends UniverseObject{
	
	private Action action;
	private Time time;
	
	public boolean equals(Object object) {
		return object != null && object instanceof ActionInvocation ? ((ActionInvocation)object).getId() == id : false;
	}
	
	public int hashCode() {
		return id;
	}
	
}
