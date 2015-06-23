package pl.edu.pw.mini.msi.knowledgerepresentation.engine;

import pl.edu.pw.mini.msi.knowledgerepresentation.data.Fluent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rwyka on 5/10/15.
 */
public class EngineH {
    public List<Map<String, Fluent>> H;
    public List<Map<String, Fluent>> Hspecial;

    public EngineH(int t) {
        H = new ArrayList<>();
        Hspecial = new ArrayList<>();
        for (int i = 0; i < t; i++) {
            H.add(new HashMap<String, Fluent>());
        }
    }

    public String toString() {
        String S = "";
        for (int i = 0; i < H.size(); i++) {
            for (Fluent fl : H.get(i).values()) {
                S += i + ". " + fl.toString() + "\n";
            }
        }
        return S;
    }

    public boolean Set(Fluent f, int t) {
        Map<String, Fluent> Map = H.get(t);
        boolean r = Map.containsKey(f.getName());
        Map.put(f.getName(), f);
        return r;
    }

    public void SetAlways(List<Fluent> fl) {
        Map<String, Fluent> hm = new HashMap<>();
        for (Fluent f : fl) {
            hm.put(f.getName(), f);
        }
        Hspecial.add(hm);
    }

    private int hist(Fluent f, int t) {
        Map<String, Fluent> Map = H.get(t);
        if (Map.containsKey(f.getName())) {
            return Map.get(f.getName()).value() == f.value() ? 1 : 0;
        }
        return -1;
    }

    public boolean Is(Fluent f, int t) {
        for (int time = t; time >= 0; time--) {
            int r = hist(f, time);
            if (r == 0)
                return false;
            else if (r == 1)
                return true;
        }
        return false;
    }

    public boolean IsTrue(List<Fluent> fl, int t) {
        if (Hspecial.size() != 0) {
            HashMap<String, Fluent> hs = new HashMap<String, Fluent>();
            for (Fluent f : fl)
                hs.put(f.getName(), f);

            for (Map<String, Fluent> aHspecial : Hspecial) {
                if (aHspecial.equals(hs))
                    return true;
            }
        }

        for (Fluent f : fl) {
            if (!Is(f, t))
                return false;
        }
        return true;
    }

    public EngineH clone() {
        EngineH n = new EngineH(H.size());
        for (int i = 0; i < H.size(); ++i) {
            for (Fluent f : H.get(i).values()) {
                n.H.get(i).put(f.getName(), f);
            }
        }

        for (int i = 0; i < Hspecial.size(); ++i) {
            HashMap<String, Fluent> hmap = new HashMap<String, Fluent>();
            for (Fluent f : Hspecial.get(i).values()) {
                hmap.put(f.getName(), f);
            }
            n.Hspecial.add(hmap);
        }

        return n;
    }


}
