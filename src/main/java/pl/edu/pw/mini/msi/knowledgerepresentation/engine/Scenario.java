package pl.edu.pw.mini.msi.knowledgerepresentation.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class Scenario {
    public List<ScenarioACSPart> ACS = new ArrayList<ScenarioACSPart>();
    public List<ScenarioOBSPart> OBS = new ArrayList<ScenarioOBSPart>();

    public Scenario(boolean fakeData) {
    	
    	if(fakeData){
	        Action a = new Action("Janek", "takesCard");
	        Action b = new Action("Janek", "locksTheDoor");
	        Action c = new Action("Janek", "comeback");
	        Action d = new Action("Janek", "leaves");
	        
	        ACS.add(new ScenarioACSPart(a, 1));
	        ACS.add(new ScenarioACSPart(d, 2));
	        ACS.add(new ScenarioACSPart(b, 3));
	        ACS.add(new ScenarioACSPart(c, 10));
	        
	        FluentList fa = new FluentList();
	        fa.Add(new Fluent("hasCard", true));
	        fa.Add(new Fluent("inHostel", true));
	        
	        FluentList fb = new FluentList();
	        fb.Add(new Fluent("hasCard", false));
	        
	        OBS.add(new ScenarioOBSPart(fa, 4));
	        OBS.add(new ScenarioOBSPart(fb, 10));
    	}
    }
    
    public void addScenarioACSPart(String actorName, String operationName, int time){
    	ACS.add(new ScenarioACSPart(new Action(actorName, operationName), time));
    }
    
    public void addScenarioOBSPart(Set<Fluent> fluents, int time){
    	FluentList fluentList = new FluentList();
    	for(Fluent fluent : fluents){
    		fluentList.Add(fluent);
    	}
    	
    	 OBS.add(new ScenarioOBSPart(fluentList, time));
    	 
    }
    
}
