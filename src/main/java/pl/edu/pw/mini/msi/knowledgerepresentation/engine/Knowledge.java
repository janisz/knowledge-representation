package pl.edu.pw.mini.msi.knowledgerepresentation.engine;

import java.util.ArrayList;
import java.util.List;


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

    public Knowledge(boolean fakeData) {
    	
    	
    	if(fakeData){
	        /*_Initially.Add(new Fluent("roomClosed", false));
	        _Initially.Add(new Fluent("hostelClosed", false));
	        _Initially.Add(new Fluent("inHostel", true));
	        _Initially.Add(new Fluent("hasCard", false));
	
	        //Action Janek_closesDoor = new Action("Janek", "closesDoor");
	        Action Janek_locksTheDoor = new Action("Janek", "locksTheDoor");
	        Action Janek_takesCard = new Action("Janek", "takesCard");
	        Action Janek_leaves = new Action("Janek", "leaves");
	        Action Janek_comeBack = new Action("Janek", "comeBack");
	
	        EffectCauses ec_a = new EffectCauses(Janek_locksTheDoor, new FluentList(new Fluent("hostelClosed", true)), new FluentList());
	        EffectCauses ec_b = new EffectCauses(Janek_takesCard, new FluentList(new Fluent("hasCard", true)), new FluentList());
	        EffectCauses ec_c = new EffectCauses(Janek_leaves, new FluentList(new Fluent("inHostel", false)), new FluentList());
	        EffectCauses ec_d = new EffectCauses(Janek_comeBack, new FluentList(new Fluent("inHostel", true)), new FluentList(new Fluent("hasCard", true)));
	
	        EffectTriggers et_a = new EffectTriggers(new FluentList(new Fluent("hasCard", false)), Janek_takesCard);
	        EffectTriggers et_b = new EffectTriggers(new FluentList(new Fluent("drunk", true)), Janek_leaves);
	
	        EffectInvokes ei_a = new EffectInvokes(Janek_leaves, Janek_locksTheDoor, 1, new FluentList());
	
	        _Couses.add(ec_a);
	        _Couses.add(ec_b);
	        _Couses.add(ec_c);
	        _Couses.add(ec_d);
	
	        _Invokes.add(ei_a);
	
	        _Triggers.add(et_a);
	        //_Triggers.add(et_b);
	
	        FluentList always_a = new FluentList(new Fluent("drunk", true));
	        //_AlwaysList.add(always_a);
	
	
	        //_ListOfAllActions.add(Janek_closesDoor);
	        _ListOfAllActions.add(Janek_locksTheDoor);
	        _ListOfAllActions.add(Janek_takesCard);
	        _ListOfAllActions.add(Janek_leaves);
	        _ListOfAllActions.add(Janek_comeBack);
	
	
	
	        */
	        /*
	        initially  [dogHungry, -bowlFul]
	        [dogHungry, bowlFull] triggers (dog, eats)
	        (dog, eats) causes [-bowlFull] if [bowlFull, dogHungry]
	        [dogHungry, -bowlFull] triggers (dog, yelp)
	        (dog, yelp) invokes (owner, makeBowlFull) after 5 if [-bowlFull]
	        (owner, makeBowlFull) causes [bowlFull]
	        */
	    	
	    	
	    	
	        _Initially.Add(new Fluent("dogHungry", true));
	        _Initially.Add(new Fluent("bowlFull", false));
	        Action dog_eats = new Action("dog", "eats");
	        Action dog_yelp = new Action("dog", "yelp");
	        Action owner_makeBowlFull = new Action("owner", "makeBowlFull");
	        Action dog_commitSuicide = new Action("dog", "commitSuicide");
	        EffectCauses ec_a = new EffectCauses(dog_eats, new FluentList(new Fluent("bowlFull", false), new Fluent("dogHungry", false)), new FluentList(new Fluent("bowlFull", true), new Fluent("dogHungry", true)));
	        EffectCauses ec_b = new EffectCauses(owner_makeBowlFull, new FluentList(new Fluent("bowlFull", true)), new FluentList());
	        EffectInvokes ei_a = new EffectInvokes(dog_yelp, owner_makeBowlFull, 1, new FluentList(new Fluent("bowlFull", false)));
	        EffectTriggers et_a = new EffectTriggers(new FluentList(new Fluent("bowlFull", true), new Fluent("dogHungry", true)), dog_eats);
	        EffectTriggers et_b = new EffectTriggers(new FluentList(new Fluent("bowlFull", false), new Fluent("dogHungry", true)), dog_yelp);
	        
	        EffectCauses tec_a = new EffectCauses(dog_yelp, new FluentList(new Fluent("dogSad", true)), new FluentList());
	        EffectCauses tec_b = new EffectCauses(dog_commitSuicide, new FluentList(new Fluent("dogDead", true)), new FluentList());
	        EffectTriggers tet_a = new EffectTriggers(new FluentList(new Fluent("dogSad", true)), dog_commitSuicide);
	
	        
	        _Couses.add(ec_a);
	        _Couses.add(ec_b);
	        _Invokes.add(ei_a);
	        _Triggers.add(et_a);
	        _Triggers.add(et_b);
	
	        _TypicallyCouses.add(tec_a);
	        _TypicallyCouses.add(tec_b);
	        _TypicallyTriggers.add(tet_a);
    	}
        
        
    }
    
    public void addFluent(String fluentName, boolean fluentValue){
    	_Initially.Add(new Fluent(fluentName, fluentValue));
    }
    
    public void addEffectCauses(EffectCauses effectCauses){
    	_Couses.add(effectCauses);
    }
    
    public void addEffectInvokes(EffectInvokes effectInvokes){
    	_Invokes.add(effectInvokes);
    }
    
    public void addEffectTriggers(EffectTriggers effectTriggers){
    	_Triggers.add(effectTriggers);
    }
    
    public void addTypicallyCauses(EffectCauses effectCauses){
    	_TypicallyCouses.add(effectCauses);
    }
    
    public void addTypicallyTriggers(EffectTriggers effectTriggers){
    	_TypicallyTriggers.add(effectTriggers);
    }
    
}
