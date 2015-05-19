
package pl.edu.pw.mini.msi.knowledgerepresentation.engine;

import pl.edu.pw.mini.msi.knowledgerepresentation.data.Fluent;

import java.util.List;

/**
 * Created by rwyka on 5/10/15.
 */
public class ScenarioOBSPart {
    public List<Fluent> _Fluents;
    public int _Time;

    public ScenarioOBSPart(List<Fluent> fluents, int time) {
        _Fluents = fluents;
        _Time = time;
    }
}
