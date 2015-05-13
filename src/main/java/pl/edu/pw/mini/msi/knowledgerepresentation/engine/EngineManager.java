package pl.edu.pw.mini.msi.knowledgerepresentation.engine;

import java.util.ArrayList;
import java.util.List;

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
