package pl.edu.pw.mini.msi.knowledgerepresentation.engine;

import pl.edu.pw.mini.msi.knowledgerepresentation.data.Action;
import pl.edu.pw.mini.msi.knowledgerepresentation.data.Fluent;

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

    public Knowledge() {

    }
}
