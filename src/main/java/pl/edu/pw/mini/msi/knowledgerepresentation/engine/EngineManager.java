package pl.edu.pw.mini.msi.knowledgerepresentation.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import pl.edu.pw.mini.msi.knowledgerepresentation.ActionLanguageListener.QueryType;
import pl.edu.pw.mini.msi.knowledgerepresentation.data.Action;
import pl.edu.pw.mini.msi.knowledgerepresentation.data.Actor;
import pl.edu.pw.mini.msi.knowledgerepresentation.data.Fluent;
import pl.edu.pw.mini.msi.knowledgerepresentation.data.Time;

/**
 * Created by rwyka on 5/12/15.
 */
public class EngineManager {
    public List<Engine> _ActiveEngines = new ArrayList<Engine>();
    public List<Engine> _Finished = new ArrayList<Engine>();
    public List<Engine> _Faulty = new ArrayList<Engine>();
    
    public EngineManager(Knowledge K, Scenario S, int T) {
        Engine e = new Engine(K, S, T);
        _ActiveEngines.add(e);
    }
    
    /**
     * Method is invoked in case when user ask a question to the system like: "always [hasCard] at 51 when scenario".
     * 
     * @param questionType Always/Ever/Typically
     * @param fluents List of checked fluents
     * @param time Checking time
     * @param scenarioName The name of scenario 
     */
    public void conditionAt(QueryType queryType, Collection<Fluent> fluents, Time time, String scenarioName){
    	switch(queryType){
    		case ALWAYS:
    				
    			break;
    		case EVER:
    			
    			break;
    		case GENERALLY:
    			
    			break;
    	}
    }
    
    /**
     * Method is invoked in case when user ask a question to the system like: "always performed (Janek,action) at 4 when sce".
     * 
     * @param questionType Always/Ever/Typically
     * @param action
     * @param time
     * @param scenarioName
     */
    public void performed(QueryType queryType, Action action, Time time, String scenarioName){
    	switch(queryType){
			case ALWAYS:
					
				break;
			case EVER:
				
				break;
			case GENERALLY:
				
				break;
		}
    }
    
    /**
     * Method is invoked in case when user ask a question to the system like: "always involved [ DoorKepper ] when scenarioOne".
     * 
     * @param questionType Always/Ever/Typically
     * @param actors
     * @param scenarioName
     */
    public void involved(QueryType queryType, Collection<Actor> actors, String scenarioName){
    	
    	switch(queryType){
			case ALWAYS:
					
				break;
			case EVER:
				
				break;
			case GENERALLY:
				
				break;
		}
    	
    }
    
    public void Run() {


        while (_ActiveEngines.size() > 0) {
            List<Engine> temp = new ArrayList<Engine>();

            for (Engine e : _ActiveEngines) {
                StepResult sr = e.Step();
                if (sr.end) {
                    _Finished.add(e);
                } else if (sr.fork) {
                    temp.add(sr.engineL);
                    temp.add(sr.engineR);
                } else {
                    temp.add(e);
                }
            }

            _ActiveEngines = temp;

        }


    }


}
