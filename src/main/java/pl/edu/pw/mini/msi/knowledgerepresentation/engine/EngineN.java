
package pl.edu.pw.mini.msi.knowledgerepresentation.engine;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import pl.edu.pw.mini.msi.knowledgerepresentation.data.*;

/**
 * Created by rwyka on 5/11/15.
 */
public class EngineN {
    public List<List<Action>> O;

    public EngineN(int t){
        O = new ArrayList<List<Action>>();
        for(int i = 0; i < t; ++i){
            O.add(new ArrayList<Action>());
        }
    }

    public void  Add(Action a, int t){
            O.get(t).add(a);
    }

    public EngineN clone(){
        EngineN n = new EngineN(O.size());
        for(int i = 0; i < O.size(); ++i){
            for(Action a : O.get(i)) {
                n.O.get(i).add(a);
            }
        }
        return n;
    }
}
