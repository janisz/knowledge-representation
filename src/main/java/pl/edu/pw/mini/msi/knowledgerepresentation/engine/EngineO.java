package pl.edu.pw.mini.msi.knowledgerepresentation.engine;
import java.util.*;
import pl.edu.pw.mini.msi.knowledgerepresentation.data.*;

/**
 * Created by rwyka on 5/11/15.
 */
public class EngineO {
    public List<AbstractMap<String, Fluent>> O;

    public EngineO(int t){
        O = new ArrayList<AbstractMap<String, Fluent>>();
        for(int i = 0; i < t; ++i){
            O.add(new HashMap<String, Fluent>());
        }
    }

    public void  Add(List<Fluent> fl, int t){
        for (Fluent f : fl){
            O.get(t).put(f.getName(), f);
        }
    }

    public EngineO clone(){
        EngineO n = new EngineO(O.size());
        for(int i = 0; i < O.size(); ++i){
            for(Fluent f : O.get(i).values()) {
                n.O.get(i).put(f.getName(), f);
            }
        }
        return n;
    }
}
