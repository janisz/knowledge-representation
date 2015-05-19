
package pl.edu.pw.mini.msi.knowledgerepresentation.engine;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import pl.edu.pw.mini.msi.knowledgerepresentation.data.*;

/**
 * Created by rwyka on 5/10/15.
 */
public class Knowledge {
    public FluentList _Initially = new FluentList();
    public List<EffectCauses> _Couses = new ArrayList<EffectCauses>();
    public List<EffectInvokes> _Invokes = new ArrayList<EffectInvokes>();
    public List<EffectTriggers> _Triggers = new ArrayList<EffectTriggers>();

    public List<EffectCauses> _TypicallyCouses = new ArrayList<EffectCauses>();
    public List<EffectInvokes> _TypicallyInvokes = new ArrayList<EffectInvokes>();
    public List<EffectTriggers> _TypicallyTriggers = new ArrayList<EffectTriggers>();

    public List<Action> _ListOfAllActions = new ArrayList<Action>(); //tylko nazwy
    public List<Fluent> _ListOfAllFluents = new ArrayList<Fluent>(); //tylko nazwy
    public List<FluentList> _AlwaysList = new ArrayList<FluentList>();

    public Knowledge() {

    }

    private void addFluents(Collection<Fluent> fluents){
    	for(Fluent fluent : fluents){
	    	addFluent(fluent);
    	}
    }

    private void addFluent(Fluent fluent){
    	if(!_ListOfAllFluents.contains(fluent)){
    		_ListOfAllFluents.add(fluent);
    	}
    }

    private void addAction(Action action){
    	if(!_ListOfAllActions.contains(action)){
    		_ListOfAllActions.add(action);
    	}
    }

    public void addEffectCause(boolean typically, Action action, List<Fluent> efectFluents, List<Fluent> conditionFluents){
    	if(typically){
    		_TypicallyCouses.add(new EffectCauses(action, efectFluents, conditionFluents));
    	}else{
    		_Couses.add(new EffectCauses(action, efectFluents, conditionFluents));
    	}

    	addAction(action);
    	addFluents(efectFluents);
    	addFluents(conditionFluents);
    }

    public void addEffectInvokes(boolean typically, Action invokingAction, Action invokedAction, int delay, List<Fluent> conditionFluents){

    	EffectInvokes effectInvokes = new EffectInvokes(invokingAction, invokedAction, delay, conditionFluents);

    	if(typically){
    		_TypicallyInvokes.add(effectInvokes);
    	}else{
    		_Invokes.add(effectInvokes);
    	}

    	addAction(invokingAction);
    	addAction(invokedAction);
    	addFluents(conditionFluents);

    }

    public void addEffectTriggers(boolean typically, Action triggedAction, List<Fluent> conditionFluents){

    	EffectTriggers effectTriggers = new EffectTriggers(conditionFluents, triggedAction);

    	if(typically){
    		_TypicallyTriggers.add(effectTriggers);
    	}else{
    		_Triggers.add(effectTriggers);
    	}

    	addAction(triggedAction);
    	addFluents(conditionFluents);

    }

    public void addAlways(Fluent fluent){

    	FluentList fluentList = new FluentList();
    	//fluentList.add(fluent);

    	_AlwaysList.add(fluentList);

    }

    public void releases(boolean typically, Action releasingAction, Collection<Fluent> releasedFluents, int delay, Collection<Fluent> conditionFluents){

    }

    public void impossible(Action impossibleAction, int time, Collection<Fluent> conditionFluents){

    }



}
