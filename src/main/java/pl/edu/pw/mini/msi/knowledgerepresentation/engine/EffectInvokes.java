package pl.edu.pw.mini.msi.knowledgerepresentation.engine;


import pl.edu.pw.mini.msi.knowledgerepresentation.data.Action;

public class EffectInvokes {
    Action _Action;
    Action _InvokedAction;
    int _TimeDelay;
    FluentList _Condition;

    public EffectInvokes(Action action, Action invokedAction, int after, FluentList condition) {
        _Action = action;
        _InvokedAction = invokedAction;
        _TimeDelay = after;
        _Condition = condition;
    }

}
