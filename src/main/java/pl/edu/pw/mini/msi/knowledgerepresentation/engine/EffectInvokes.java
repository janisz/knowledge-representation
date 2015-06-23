package pl.edu.pw.mini.msi.knowledgerepresentation.engine;

import pl.edu.pw.mini.msi.knowledgerepresentation.data.Action;
import pl.edu.pw.mini.msi.knowledgerepresentation.data.Fluent;

import java.util.List;

/**
 * Created by rwyka on 5/10/15.
 */
public class EffectInvokes {
    Action _Action;
    Action _InvokedAction;
    int _TimeDelay;
    List<Fluent> _Condition;

    public EffectInvokes(Action action, Action invokedAction, int after, List<Fluent> condition) {
        _Action = action;
        _InvokedAction = invokedAction;
        _TimeDelay = after;
        _Condition = condition;
    }

}
