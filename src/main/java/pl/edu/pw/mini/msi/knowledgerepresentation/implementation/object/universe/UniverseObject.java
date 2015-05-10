package pl.edu.pw.mini.msi.knowledgerepresentation.implementation.object.universe;
/**
 * 
 * @author maq
 *
 */
public abstract class UniverseObject {
	
	private static int ID;
	protected int id;
	
	public UniverseObject(){
		
		if (ID == Integer.MAX_VALUE){
			ID = 0;
		}
		
		id = ID++;
		
	}

	public int getId() {
		return id;
	}
	
}

