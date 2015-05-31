
package pl.edu.pw.mini.msi.knowledgerepresentation.engine;
import java.io.Console;
import java.util.ArrayList;
import java.util.List;

import pl.edu.pw.mini.msi.knowledgerepresentation.data.Action;
import pl.edu.pw.mini.msi.knowledgerepresentation.data.Fluent;
import pl.edu.pw.mini.msi.knowledgerepresentation.data.Scenario;
/**
 * Created by rwyka on 5/19/15.
 */
public class Environment {
    Knowledge _K;
    List<Scenario> _S;
    List<EngineManager> _EM;


    public Environment(Knowledge K){
        _K = K;
        _S = new ArrayList<Scenario>();
        _EM = new ArrayList<EngineManager>();
    }

    public int AddScenario(Scenario S, int T){
        _S.add(S);
        _EM.add(new EngineManager(_K, S, T));
        return _S.size();
    }


    public boolean QueryConditionEver(List<Fluent> condition, int time, Scenario scenario){
        return _EM.get(_S.indexOf(scenario)).ConditionAllways(condition, time);
    }

    public void QueryConditionTypically(List<Fluent> condition, int time, Scenario scenario){
    	
    }

    public boolean QueryConditionAllways(List<Fluent> condition, int time, Scenario scenario){
        return _EM.get(_S.indexOf(scenario)).ConditionEver(condition, time);
    }

    public boolean QueryActionEver(Action action, int time, Scenario scenario){
        System.out.println(scenario.toString());
        System.out.println(_S.toString());
        return _EM.get(_S.indexOf(scenario)).ActionAllways(action, time);
    }
    
    public void QueryActionTypically(Action action, int time, Scenario scenario){

    }

    public boolean QueryActionAllwaysr(Action action, int time, Scenario scenario){
        return _EM.get(_S.indexOf(scenario)).ActionEver(action, time);
    }


    public void QueryInvolvedEver(Fluent fluent, int time, Scenario scenario){

    }

    public void QueryInvolvedAllways(Fluent fluent, int time, Scenario scenario){

    }


}
