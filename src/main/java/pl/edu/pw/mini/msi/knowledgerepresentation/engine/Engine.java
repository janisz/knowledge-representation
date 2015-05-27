
package pl.edu.pw.mini.msi.knowledgerepresentation.engine;

import pl.edu.pw.mini.msi.knowledgerepresentation.data.Fluent;
import pl.edu.pw.mini.msi.knowledgerepresentation.data.Scenario;
import pl.edu.pw.mini.msi.knowledgerepresentation.data.ScenarioACSPart;
import pl.edu.pw.mini.msi.knowledgerepresentation.data.ScenarioOBSPart;

import java.util.List;


/**
 * Created by rwyka on 5/10/15.
 */
public class Engine {
    Knowledge _K;
    Scenario _S;

    EngineE _E;
    EngineH _H;
    EngineO _O;
    EngineN _N;
    int _TMax;

    int _currentTime;
    int _currentPart;
    int _currentSubstep;
    boolean _end = false;

    public static  int _Number = 0;
    public  int _EngineNumber = 0;

    public Engine(Knowledge K, Scenario S, int TMax){
        _K = K;
        _S = S;

        _TMax = TMax;
        _currentTime = 0;
        _currentPart = 0;
        _currentSubstep = 0;

        _E = new EngineE(_TMax+1);
        _H = new EngineH(_TMax+1);
        _O = new EngineO(_TMax+1);
        _N = new EngineN(_TMax+1);

        LoadInitially();
        //LoadFluentsFromScenario();
        //LoadActionCallsFromScenario();
        LoadAlways();
        _EngineNumber = _Number;
        _Number++;
    }

    public Engine(Engine e){
        _K = e._K;
        _S = e._S;
        _E = e._E.clone();
        _H = e._H.clone();
        _O = e._O.clone();
        _N = e._N.clone();

        _TMax = e._TMax;
        _currentPart = e._currentPart;
        _currentTime = e._currentTime;
        _currentSubstep = e._currentSubstep;
        _end= e._end;
        _EngineNumber = _Number;
        _Number++;
    }

    public StepResult Step(){
        if(_end) return StepResult.Normal(this);

        if(_currentPart == 0){
            boolean r = CheckTypicallyTriggersSubstep(_currentSubstep, _currentTime);

            if(r){
                Engine fork = this.clone();
                fork.UpdateTypicallyTriggersSubstep(fork._currentSubstep, fork._currentTime);
                fork._currentSubstep = fork.GetTypicallyTriggersNextSubstep(fork._currentSubstep, fork._currentTime);
                if(fork._currentSubstep == 0)
                    fork._currentPart++;

                _currentSubstep = GetTypicallyTriggersNextSubstep(_currentSubstep, _currentTime);
                if(_currentSubstep == 0)
                    _currentPart++;

                return StepResult.Fork(this, fork);
            }

            _currentSubstep = GetTypicallyTriggersNextSubstep(_currentSubstep, _currentTime);
            if(_currentSubstep == 0)
                _currentPart++;
        }else if(_currentPart == 1){
            _currentSubstep = UpdateTriggersSubstep(_currentSubstep, _currentTime);
            if(_currentSubstep == 0)
                _currentPart++;
        }else if(_currentPart == 2){
            boolean r = CheckTypicallyCausesSubstep(_currentSubstep, _currentTime);

            if(r){
                Engine fork = this.clone();
                fork.UpdateTypicallyCausesSubstep(fork._currentSubstep, fork._currentTime);
                fork._currentSubstep = fork.GetTypicallyCausesNextSubstep(fork._currentSubstep, fork._currentTime);
                if(fork._currentSubstep == 0)
                    fork._currentPart++;

                _currentSubstep = GetTypicallyCausesNextSubstep(_currentSubstep, _currentTime);
                if(_currentSubstep == 0)
                    _currentPart++;

                return StepResult.Fork(this, fork);
            }

            _currentSubstep = GetTypicallyCausesNextSubstep(_currentSubstep, _currentTime);
            if(_currentSubstep == 0)
                _currentPart++;
        }else if(_currentPart == 3){
            boolean r = CheckTypicallyInvokesSubstep(_currentSubstep, _currentTime);

            if(r){
                Engine fork = this.clone();
                fork.UpdateTypicallyInvokesSubstep(fork._currentSubstep, fork._currentTime);
                fork._currentSubstep = fork.GetTypicallyInvokesNextSubstep(fork._currentSubstep, fork._currentTime);
                if(fork._currentSubstep == 0)
                    fork._currentPart++;

                _currentSubstep = GetTypicallyInvokesNextSubstep(_currentSubstep, _currentTime);
                if(_currentSubstep == 0)
                    _currentPart++;

                return StepResult.Fork(this, fork);
            }

            _currentSubstep = GetTypicallyInvokesNextSubstep(_currentSubstep, _currentTime);
            if(_currentSubstep == 0)
                _currentPart++;
        }else if(_currentPart == 4){
            _currentSubstep = UpdateInvokesSubstep(_currentSubstep, _currentTime);
            if(_currentSubstep == 0)
                _currentPart++;
        }else if(_currentPart == 5){
            _currentSubstep = UpdateCausesSubstep(_currentSubstep, _currentTime);
            if(_currentSubstep == 0) {
                _currentPart = 0;
                _currentTime++;
            }
        }
        if(_currentTime > _TMax){
            _end = true;
            return StepResult.End(this);
        }

        return StepResult.Normal(this);
    }


    public void LoadInitially(){
        for(Fluent x : _K._Initially){
            _H.Set(x, 0);
        }
    }

    public void LoadFluentsFromScenario(){
        for(ScenarioOBSPart x : _S.OBS){
            for(Fluent y : x._Fluents) {
                _H.Set(y, x._Time);
            }
        }
    }

    public int UpdateCausesSubstep(int currentSubstep, int t){
        if(_K._Couses.size() == 0) return 0;
        EffectCauses ec = _K._Couses.get(currentSubstep);

        if(_E.IsIn(ec._Action, t)){
            boolean ok = _H.IsTrue(ec._Condition, t);

            if(ok){
                _O.Add(ec._Effect, t+1);
                for(Fluent f : ec._Effect){
                    _H.Set(f, t+1);
                    System.out.println("E("+_EngineNumber+") "+"F: " + t + ". action " + ec._Action + " changed fluent " + f.toString() + " at " + (t+1));
                }
            }

        }

        return (currentSubstep + 1) % _K._Couses.size();
    }

    public boolean CheckTypicallyCausesSubstep(int currentSubstep, int t){
        if(_K._TypicallyCouses.size() == 0) return false;
        EffectCauses ec = _K._TypicallyCouses.get(currentSubstep);

        if(_E.IsIn(ec._Action, t)){
            boolean ok = _H.IsTrue(ec._Condition, t);

            if(ok){
                return true;
            }
        }

        return false;
    }

    public int GetTypicallyCausesNextSubstep(int currentSubstep, int t){
        if(_K._TypicallyCouses.size() == 0) return 0;
        return (currentSubstep + 1) % _K._TypicallyCouses.size();
    }

    public void UpdateTypicallyCausesSubstep(int currentSubstep, int t){
        EffectCauses ec = _K._TypicallyCouses.get(currentSubstep);

        _O.Add(ec._Effect, t+1);
        for(Fluent f : ec._Effect){
            _H.Set(f, t+1);
            System.out.println("E("+_EngineNumber+") "+"F: " + t + ". action " + ec._Action + " changed fluent " + f.toString() + " at " + (t+1));
        }
    }

    public int UpdateInvokesSubstep(int currentSubstep, int t){
        if(_K._Invokes.size() == 0) return 0;
        EffectInvokes ei = _K._Invokes.get(currentSubstep);

        if(_E.IsIn(ei._Action, t)){
            boolean ok = _H.IsTrue(ei._Condition, t);

            if(ok && t + ei._TimeDelay < _TMax) {
                boolean res = _E.Set(ei._InvokedAction, t + ei._TimeDelay);
                if(res) System.out.println("E("+_EngineNumber+") "+"A: " + t + ". action " + ei._Action + " invoked " + ei._InvokedAction.toString() + " after " + ei._TimeDelay + ", so at " + (t + ei._TimeDelay));
                if(!res) System.out.println("E("+_EngineNumber+") "+"A: " + t + ". action invoke omitted " + ei._InvokedAction.toString() + " after " + ei._TimeDelay + ", so at " + (t + ei._TimeDelay));
            }

        }
        return (currentSubstep + 1) % _K._Invokes.size();
    }

    public boolean CheckTypicallyInvokesSubstep(int currentSubstep, int t){
        if(_K._TypicallyInvokes.size() == 0) return false;
        EffectInvokes ei = _K._TypicallyInvokes.get(currentSubstep);

        if(_E.IsIn(ei._Action, t)){
            boolean ok = _H.IsTrue(ei._Condition, t);

            if(ok && t + ei._TimeDelay < _TMax && _E.SetPossible(ei._InvokedAction, t+ei._TimeDelay)) {
                return true;
            }

        }
        return false;
    }

    public int GetTypicallyInvokesNextSubstep(int currentSubstep, int t){
        if(_K._TypicallyInvokes.size() == 0) return 0;
        return (currentSubstep + 1) % _K._TypicallyInvokes.size();
    }

    public void UpdateTypicallyInvokesSubstep(int currentSubstep, int t){
        EffectInvokes ei = _K._TypicallyInvokes.get(currentSubstep);

        boolean res = _E.Set(ei._InvokedAction, t + ei._TimeDelay);
        if(res) _N.Add(ei._InvokedAction, t + ei._TimeDelay);
        if(res) System.out.println("E("+_EngineNumber+") "+"A: " + t + ". action " + ei._Action + " invoked " + ei._InvokedAction.toString() + " after " + ei._TimeDelay + ", so at " + (t + ei._TimeDelay));
        if(!res) System.out.println("E("+_EngineNumber+") "+"A: " + t + ". action invoke omitted " + ei._InvokedAction.toString() + " after " + ei._TimeDelay + ", so at " + (t + ei._TimeDelay));
    }

    public int UpdateTriggersSubstep(int currentSubstep, int t){
        if(_K._Triggers.size() == 0) return 0;
        EffectTriggers et = _K._Triggers.get(currentSubstep);

        boolean ok = _H.IsTrue(et._Condition, t);

        if(ok && t + 1 < _TMax) {
            boolean res = _E.Set(et._TriggeredAction, t);
            if(res) System.out.println("E("+_EngineNumber+") "+"A: " + t + ". action triggered " + et._TriggeredAction.toString());
            if(!res) System.out.println("E("+_EngineNumber+") "+"A: " + t + ". action trigger omitted " + et._TriggeredAction.toString());
        }

        return (currentSubstep + 1) % _K._Triggers.size();
    }

    public boolean CheckTypicallyTriggersSubstep(int currentSubstep, int t){
        if(_K._TypicallyTriggers.size() == 0) return false;
        EffectTriggers et = _K._TypicallyTriggers.get(currentSubstep);

        boolean ok = _H.IsTrue(et._Condition, t);

        if(ok && t + 1 < _TMax && _E.SetPossible(et._TriggeredAction, t)) {
            return true;
        }
        return false;
    }

    public int GetTypicallyTriggersNextSubstep(int currentSubstep, int t){
        if(_K._TypicallyTriggers.size() == 0) return 0;
        return (currentSubstep + 1) % _K._TypicallyTriggers.size();
    }

    public void UpdateTypicallyTriggersSubstep(int currentSubstep, int t){
        EffectTriggers et = _K._TypicallyTriggers.get(currentSubstep);

        boolean res = _E.Set(et._TriggeredAction, t);
        if(res) _N.Add(et._TriggeredAction, t);
        if(res) System.out.println("E("+_EngineNumber+") "+"A: " + t + ". action triggered " + et._TriggeredAction.toString());
        if(!res) System.out.println("E("+_EngineNumber+") "+"A: " + t + ". action trigger omitted " + et._TriggeredAction.toString());
    }

    public void LoadActionCallsFromScenario(){
        for(ScenarioACSPart x : _S.ACS){
            _E.Set(x._Action, x._Time);
        }
    }

    public void LoadAlways(){
        for(List<Fluent> fl : _K._AlwaysList){
            _H.SetAlways(fl);
        }
    }


    public Engine clone(){
        return new Engine(this);
    }
}
