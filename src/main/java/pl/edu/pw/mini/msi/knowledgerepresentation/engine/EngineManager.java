
package pl.edu.pw.mini.msi.knowledgerepresentation.engine;
import java.util.ArrayList;
import java.util.List;

import pl.edu.pw.mini.msi.knowledgerepresentation.data.Action;
import pl.edu.pw.mini.msi.knowledgerepresentation.data.Fluent;
import pl.edu.pw.mini.msi.knowledgerepresentation.data.Scenario;

/**
 * Created by rwyka on 5/12/15.
 */
public class EngineManager {
    public List<Engine> _ActiveEngines = new ArrayList<Engine>();
    public List<Engine> _Finished = new ArrayList<Engine>();
    public List<Engine> _Faulty = new ArrayList<Engine>();
    public boolean _Calculated = false;

    public EngineManager(Knowledge K, Scenario S, int T){
        Engine e = new Engine(K, S, T);
    	_ActiveEngines.add(e);
    }


    public void Run(){
        if(_Calculated) return;

        while(_ActiveEngines.size() > 0){
            List<Engine> temp = new ArrayList<Engine>();

            for(Engine e : _ActiveEngines){
                StepResult sr = e.Step();
                if(sr.end){
                    _Finished.add(e);
                }else if(sr.fork){
                    temp.add(sr.engineL);
                    temp.add(sr.engineR);
                }else {
                    temp.add(e);
                }
            }

            _ActiveEngines = temp;

        }

        _Calculated = true;
    }


    boolean ActionAllways(Action action, int time){
        Run();
        for(Engine e : _Finished){
           if(!e._E.IsIn(action, time)) return false;
        }
        return true;
    }

    boolean ActionEver(Action action, int time){
        Run();
        for(Engine e : _Finished){
           if(e._E.IsIn(action, time)) return true;
        }
        return false;
    }

    boolean ConditionAllways(List<Fluent> condition, int time){
        Run();
        for(Engine e : _Finished){
            if(!e._H.IsTrue(condition, time)) return false;
        }
        return true;
    }

    boolean ConditionEver(List<Fluent> condition, int time){
        Run();
        for(Engine e : _Finished){
            if(e._H.IsTrue(condition, time)) return true;
        }
        return false;
    }




}
