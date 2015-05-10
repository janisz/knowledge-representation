package pl.edu.pw.mini.msi.knowledgerepresentation.implementation.object;

import pl.edu.pw.mini.msi.knowledgerepresentation.implementation.Universe;
import pl.edu.pw.mini.msi.knowledgerepresentation.implementation.object.universe.Actor;
import pl.edu.pw.mini.msi.knowledgerepresentation.implementation.object.universe.Fluent;
/**
 * 
 * @author maq
 *
 */
public class Tester {
	
	public static void main(String[] args) {
		Tester tester = new Tester();
		
		Universe universe = new Universe();
		
		
		
	}
	
	private void initFLuents(Universe universe){
		Fluent roomClosedFluent = new Fluent("roomClosed");
		Fluent hotelClosedFluent = new Fluent("hotelClosed");
		
		universe.addFluent(roomClosedFluent);
		universe.addFluent(hotelClosedFluent);
		
		Actor janekActor = new Actor("Janek");
		
		universe.addActor(janekActor);
		
		
		
	}
	
}

