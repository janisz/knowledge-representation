package pl.edu.pw.mini.msi.knowledgerepresentation.engine;


import java.util.ArrayList;
import java.util.List;


public class FluentList {

    List<Fluent> list = new ArrayList<Fluent>();

    public FluentList() {

    }

    public FluentList(Fluent a) {
        Add(a);
    }

    public FluentList(Fluent a, Fluent b) {
        Add(a);
        Add(b);
    }

    public FluentList(Fluent a, Fluent b, Fluent c) {
        Add(a);
        Add(b);
        Add(c);
    }

    public void Add(Fluent F) {
        for (Fluent f : list) {
            if (f.SameName(F)) return;
        }
        list.add(F);
    }

    public boolean Same(FluentList fl) {
        if (fl.list.size() != list.size())
            return false;

        int count = 0;
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.size(); j++)
                if (!list.get(i).equals(fl.list.get(j))) {
                    count++;
                    if (list.get(i)._Value != fl.list.get(i)._Value)
                        return false;
                }
        }

        return count == list.size();
    }

    public FluentList clone() {
        FluentList fl = new FluentList();
        for (Fluent f : list) {
            fl.Add(f.clone());
        }
        return fl;
    }
}
