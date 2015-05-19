
package pl.edu.pw.mini.msi.knowledgerepresentation.engine;
import java.util.ArrayList;
import java.util.List;
import pl.edu.pw.mini.msi.knowledgerepresentation.data.*;

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


    public boolean QueryConditionEver(List<Fluent> condition, int time, int scenario){
        return _EM.get(scenario).ConditionAllways(condition, time);
    }

    public void QueryConditionTypically(List<Fluent> condition, int time, int scenario){

    }

    public boolean QueryConditionAllways(List<Fluent> condition, int time, int scenario){
        return _EM.get(scenario).ConditionEver(condition, time);
    }


    public boolean QueryActionEver(Action action, int time, int scenario){
        return _EM.get(scenario).ActionAllways(action, time);
    }

    public void QueryActionTypically(Action action, int time, int scenario){

    }

    public boolean QueryActionAllwaysr(Action action, int time, int scenario){
        return _EM.get(scenario).ActionEver(action, time);
    }


    public void QueryInvolvedEver(Fluent fluent, int time, int scenario){

    }

    public void QueryInvolvedAllways(Fluent fluent, int time, int scenario){

    }


}
