package pl.edu.pw.mini.msi.knowledgerepresentation.data;

/**
 * Created by rwyka on 5/10/15.
 */
public class ScenarioACSPart {
    public final Action _Action;
    public final int _Time;

    public ScenarioACSPart(Action action, int time) {
        _Action = action;
        _Time = time;

    }

    @Override
    public String toString() {
        return "ScenarioACSPart{" +
                "_Action=" + _Action +
                ", _Time=" + _Time +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ScenarioACSPart that = (ScenarioACSPart) o;

        if (_Time != that._Time) return false;
        return !(_Action != null ? !_Action.equals(that._Action) : that._Action != null);

    }

    @Override
    public int hashCode() {
        int result = _Action != null ? _Action.hashCode() : 0;
        result = 31 * result + _Time;
        return result;
    }
}
