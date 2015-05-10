package pl.edu.pw.mini.msi.knowledgerepresentation.implementation.object.universe;
/**
 * 
 * @author maq
 * 
 */
public class ActionResult extends UniverseObject{
	
	private Action action;
	private Fluent fluent;
	private boolean value;
	
	public ActionResult(Action action, Fluent fluent, boolean value) {
		this.action = action;
		this.fluent = fluent;
		this.value = value;
	}
	
	public Fluent getFluent() {
		return fluent;
	}
	
	public Action getAction() {
		return action;
	}
	
	public boolean isValue() {
		return value;
	}

	public boolean equals(Object object) {
		return object != null && object instanceof Action ? ((Action)object).getId() == id : false;
	}
	
	public int hashCode() {
		return id;
	}
	
}


