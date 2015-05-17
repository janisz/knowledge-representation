package pl.edu.pw.mini.msi.knowledgerepresentation.engine;

import pl.edu.pw.mini.msi.knowledgerepresentation.data.Action;

import java.util.ArrayList;
import java.util.List;


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
        for (int i = 0; i < E.get(t).size(); i++) {
            if (E.get(t).get(i).equals(a)) {
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
        for (int i = 0; i < E.get(t).size(); i++) {
            if (E.get(t).get(i).equals(a)) {
                return true;
            }
        }
        return false;
    }

    public EngineE clone() {
        EngineE n = new EngineE(E.size());
        for (int i = 0; i < E.size(); ++i) {
            for (int j = 0; j < E.get(i).size(); ++j) {
                n.Set(E.get(i).get(j), i);
            }
        }
        return n;
    }
}
