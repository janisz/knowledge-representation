package pl.edu.pw.mini.msi.knowledgerepresentation.engine;


import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import pl.edu.pw.mini.msi.knowledgerepresentation.data.Fluent;


public class FluentList implements Cloneable {

    private final Map<String, Fluent> map;

    private FluentList(Map<String, Fluent> fluents) {
        map = fluents;
    }

    public FluentList(Fluent... fluents) {
        map = new HashMap<>();
        for (Fluent fluent : fluents) {
            add(fluent);
        }
    }
    
    public FluentList(Collection<Fluent> fluents){
    	map = new HashMap<>();
        for (Fluent fluent : fluents) {
            add(fluent);
        }
    }
    
    public Collection<Fluent> getList() {
        return map.values();
    }

    public void add(Fluent fluent) {
        if (!map.containsKey(fluent.getName())) {
            map.put(fluent.getName(), fluent);
        }
    }

    @Override
    public FluentList clone() {
        return new FluentList(map);
    }
}
