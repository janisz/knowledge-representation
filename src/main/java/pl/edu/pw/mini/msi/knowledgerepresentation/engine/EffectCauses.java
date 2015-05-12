package pl.edu.pw.mini.msi.knowledgerepresentation.engine;


public class EffectCauses {
    Action _Action;
    FluentList _Condition;
    FluentList _Effect;

    public EffectCauses(Action action, FluentList effect, FluentList condition) {
        _Action = action;
        _Condition = condition;
        _Effect = effect;
    }
}
