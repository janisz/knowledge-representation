
package pl.edu.pw.mini.msi.knowledgerepresentation.engine;
import pl.edu.pw.mini.msi.knowledgerepresentation.data.*;
/**
 * Created by rwyka on 5/10/15.
 */
public class ScenarioOBSPart {
    public FluentList _Fluents;
    public int _Time;
    public ScenarioOBSPart(FluentList fluents, int time){
        _Fluents = fluents;
        _Time = time;
    }
}
