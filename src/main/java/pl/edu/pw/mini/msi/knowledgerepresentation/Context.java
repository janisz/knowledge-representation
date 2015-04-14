package pl.edu.pw.mini.msi.knowledgerepresentation;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import pl.edu.pw.mini.msi.knowledgerepresentation.data.Actor;
import pl.edu.pw.mini.msi.knowledgerepresentation.data.Fluent;
import pl.edu.pw.mini.msi.knowledgerepresentation.data.Scenario;

import java.util.Collection;
import java.util.HashMap;

public class Context {
    public final Collection<Actor> actors = Sets.newLinkedHashSet();
    public final Collection<Fluent> fluents = Sets.newLinkedHashSet();
    public final Collection<String> identifiers = Sets.newLinkedHashSet();
    public final HashMap<String, Scenario> scenarios = Maps.newLinkedHashMap();
}
