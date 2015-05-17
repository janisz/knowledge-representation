package pl.edu.pw.mini.msi.knowledgerepresentation.engine;

import pl.edu.pw.mini.msi.knowledgerepresentation.data.Fluent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rwyka on 5/11/15.
 */
public class EngineO {
    public List<FluentList> O;

    public EngineO(int t) {
        O = new ArrayList<FluentList>();
        for (int i = 0; i < t; ++i) {
            O.add(new FluentList());
        }
    }

    public void Add(FluentList fl, int t) {
        for (Fluent f : fl.getList()) {
            O.get(t).add(f);
        }
    }

    public EngineO clone() {
        EngineO n = new EngineO(O.size());
        for (int i = 0; i < O.size(); ++i) {
            n.O.add(O.get(i).clone());
        }
        return n;
    }
}
