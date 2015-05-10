package pl.edu.pw.mini.msi.knowledgerepresentation.implementation.object.universe;
/**
 * 
 * @author maq
 *
 */
public class Action extends UniverseObject{
	
	private Actor actor;
	private ActionOperation actionOperation;
	
	public Action(Actor actor, ActionOperation actionOperation) {
		this.actor = actor;
		this.actionOperation = actionOperation;
	}
	
	public Actor getActor() {
		return actor;
	}
	
	public ActionOperation getActionOperation() {
		return actionOperation;
	}
	
	public boolean equals(Object object) {
		return object != null && object instanceof Action ? ((Action)object).getId() == id : false;
	}
	
	public int hashCode() {
		return id;
	}
	
}
