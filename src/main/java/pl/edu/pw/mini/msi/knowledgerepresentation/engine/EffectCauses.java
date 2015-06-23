package pl.edu.pw.mini.msi.knowledgerepresentation.engine;

import pl.edu.pw.mini.msi.knowledgerepresentation.data.Action;
import pl.edu.pw.mini.msi.knowledgerepresentation.data.Fluent;

import java.util.List;

/**
 * Created by rwyka on 5/10/15.
 */
public class EffectCauses {
    Action _Action;
    List<Fluent> _Condition;
    List<Fluent> _Effect;

    public EffectCauses(Action action, List<Fluent> effect, List<Fluent> condition) {
        _Action = action;
        _Condition = condition;
        _Effect = effect;
    }
}
