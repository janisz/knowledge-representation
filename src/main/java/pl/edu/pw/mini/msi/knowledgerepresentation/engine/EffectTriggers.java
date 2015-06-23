package pl.edu.pw.mini.msi.knowledgerepresentation.engine;

import pl.edu.pw.mini.msi.knowledgerepresentation.data.Action;
import pl.edu.pw.mini.msi.knowledgerepresentation.data.Fluent;

import java.util.List;

/**
 * Created by rwyka on 5/10/15.
 */
public class EffectTriggers {
    List<Fluent> _Condition;
    Action _TriggeredAction;

    public EffectTriggers(List<Fluent> condition, Action triggeredAction) {
        _Condition = condition;
        _TriggeredAction = triggeredAction;
    }
}
