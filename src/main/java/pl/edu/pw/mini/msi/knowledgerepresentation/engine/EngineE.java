package pl.edu.pw.mini.msi.knowledgerepresentation.engine;

import pl.edu.pw.mini.msi.knowledgerepresentation.data.Action;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rwyka on 5/10/15.
 */
public class EngineE {
    public List<List<Action>> E;

    public EngineE(int t) {
        E = new ArrayList<List<Action>>();
        for (int i = 0; i < t; i++) {
            E.add(new ArrayList<Action>());
        }
    }

    public String toString() {
        String S = "";
        for (int i = 0; i < E.size(); i++) {
            for (int j = 0; j < E.get(i).size(); j++) {
                S += i + ". " + E.get(i).get(j).toString() + "\n";
            }
        }
        return S;
    }

    public boolean Set(Action a, int t) {
        boolean is = true;
        if (E.get(t).size() == 1) {
            if (E.get(t).get(0).equals(a)) {
                return true;
            }
        }
        if (E.get(t).size() == 0) {
            E.get(t).add(a);
            return true;
        }
        return false;
    }

    public boolean SetPossible(Action a, int t) {
        return E.get(t).size() == 0;
    }

    public boolean IsIn(Action a, int t) {
        if (E.get(t).size() == 1 && E.get(t).get(0).equals(a))
            return true;
        return false;
    }

    public EngineE clone() {
        EngineE n = new EngineE(E.size());
        for (int i = 0; i < E.size(); ++i) {
            if (E.get(i).size() == 1) {
                n.Set(E.get(i).get(0), i);
            }
        }
        return n;
    }
}
