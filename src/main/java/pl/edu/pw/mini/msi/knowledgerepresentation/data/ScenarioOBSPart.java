
package pl.edu.pw.mini.msi.knowledgerepresentation.data;

import pl.edu.pw.mini.msi.knowledgerepresentation.data.Fluent;

import java.util.List;

/**
 * Created by rwyka on 5/10/15.
 */
public class ScenarioOBSPart {
    public final List<Fluent> _Fluents;
    public final int _Time;

    public ScenarioOBSPart(List<Fluent> fluents, int time) {
        _Fluents = fluents;
        _Time = time;
    }

    @Override
    public String toString() {
        return "ScenarioOBSPart{" +
                "_Fluents=" + _Fluents +
                ", _Time=" + _Time +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ScenarioOBSPart that = (ScenarioOBSPart) o;

        if (_Time != that._Time) return false;
        return !(_Fluents != null ? !_Fluents.equals(that._Fluents) : that._Fluents != null);

    }

    @Override
    public int hashCode() {
        int result = _Fluents != null ? _Fluents.hashCode() : 0;
        result = 31 * result + _Time;
        return result;
    }
}
