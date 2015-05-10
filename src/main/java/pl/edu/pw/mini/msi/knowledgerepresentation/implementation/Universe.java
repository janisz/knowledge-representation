package pl.edu.pw.mini.msi.knowledgerepresentation.implementation;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import pl.edu.pw.mini.msi.knowledgerepresentation.implementation.object.universe.Action;
import pl.edu.pw.mini.msi.knowledgerepresentation.implementation.object.universe.ActionInvocation;
import pl.edu.pw.mini.msi.knowledgerepresentation.implementation.object.universe.Actor;
import pl.edu.pw.mini.msi.knowledgerepresentation.implementation.object.universe.Fluent;
import pl.edu.pw.mini.msi.knowledgerepresentation.implementation.object.universe.FluentChange;
/**
 * 
 * @author maq
 *
 */
public class Universe {
	
	private Set<Fluent> fluents;
	private Map<Fluent, Set<FluentChange>> fluentChanges;
	
	private Set<Action> actions; 
	private Map<Action, Set<ActionInvocation>> actionInvocationMap;
	
	private Set<Actor> actors;
	
	public Universe(){
		fluents = new HashSet<Fluent>();
	}
	
	public void addFluent(Fluent fluent){
		
		fluents.add(fluent);
		
	}
	
	public void addActor(Actor actor){
		actors.add(actor);
	}
	
}


