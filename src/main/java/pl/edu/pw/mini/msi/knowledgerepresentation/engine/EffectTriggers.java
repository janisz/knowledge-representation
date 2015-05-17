package pl.edu.pw.mini.msi.knowledgerepresentation.engine;


import pl.edu.pw.mini.msi.knowledgerepresentation.data.Action;

public class EffectTriggers {
    FluentList _Condition;
    Action _TriggeredAction;

    public EffectTriggers(FluentList condition, Action triggeredAction) {
        _Condition = condition;
        _TriggeredAction = triggeredAction;
    }
}
