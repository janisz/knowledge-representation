package pl.edu.pw.mini.msi.knowledgerepresentation.engine;

import pl.edu.pw.mini.msi.knowledgerepresentation.data.Fluent;

import java.util.ArrayList;
import java.util.List;


public class EngineH {
    public List<List<Fluent>> H;
    public List<List<FluentList>> Hspecial;

    public EngineH(int t) {
        H = new ArrayList<List<Fluent>>();
        Hspecial = new ArrayList<List<FluentList>>();
        for (int i = 0; i < t; i++) {
            H.add(new ArrayList<Fluent>());
            Hspecial.add(new ArrayList<FluentList>());
        }
    }

    public String toString() {
        String S = "";
        for (int i = 0; i < H.size(); i++) {
            for (int j = 0; j < H.get(i).size(); j++) {
                S += i + ". " + H.get(i).get(j).toString() + "\n";
            }
        }
        return S;
    }

    public boolean Set(Fluent f, int t) {
        boolean is = true;
        for (int i = 0; i < H.get(t).size(); i++) {
            if (H.get(t).get(i).sameName(f)) {
                H.get(t).set(i, f);
                return true;
            }
        }
        H.get(t).add(f);
        return false;
    }

    public void SetAlways(FluentList fl) {
        for (int i = 0; i < Hspecial.size(); i++)
            Hspecial.get(i).add(fl);
    }

    private int hist(Fluent f, int t) {
        for (int i = 0; i < H.get(t).size(); i++) {
            if (H.get(t).get(i).sameName(f)) {
                if (H.get(t).get(i).isPositive() == f.isPositive())
                    return 1;
                else
                    return 0;
            }
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

    public boolean IsTrue(FluentList fl, int t) {
        for (int i = 0; i < Hspecial.get(t).size(); i++) {
            if (Hspecial.get(t).get(i).equals(fl))
                return true;
        }

        for (Fluent f : fl.getList()) {
            if (!Is(f, t))
                return false;
        }
        return true;
    }

    public EngineH clone() {
        EngineH n = new EngineH(H.size());
        for (int i = 0; i < H.size(); ++i) {
            for (int j = 0; j < H.get(i).size(); ++j) {
                n.H.get(i).add(H.get(i).get(j).clone());
            }
        }

        for (int i = 0; i < Hspecial.size(); ++i) {
            for (int j = 0; j < Hspecial.get(i).size(); ++j) {
                n.Hspecial.get(i).add(Hspecial.get(i).get(j).clone());
            }
        }

        return n;
    }


}
