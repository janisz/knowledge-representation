package pl.edu.pw.mini.msi.knowledgerepresentation.implementation.object.universe;
/**
 * 
 * @author maq
 *
 */
public class ActionOperation {
	
	private static String DEFAULT_NAME = "default";
	
	private String name;
	
	public ActionOperation(String name) {
		this.name = (name != null) ? this.name = name : DEFAULT_NAME;
	}

	public String getName() {
		return name;
	}
	
	public boolean equals(Object object) {
		return object != null && object instanceof ActionOperation ? 
				((ActionOperation)object).getName().equals(name) : false;
	}
	
	public int hashCode() {
		return name.hashCode();
	}
	
}
